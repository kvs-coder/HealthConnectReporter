package com.kvs.healthconnectreporter.service

import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.asDuration
import com.kvs.healthconnectreporter.decorator.asLocalTimeRangeFilter
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asPeriod
import com.kvs.healthconnectreporter.decorator.asTimeRangeFilter
import com.kvs.healthconnectreporter.decorator.clientCall
import com.kvs.healthconnectreporter.decorator.epochMillis
import com.kvs.healthconnectreporter.decorator.harmonize
import com.kvs.healthconnectreporter.decorator.metric
import com.kvs.healthconnectreporter.model.Aggregate
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.TimeGroup
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.type.Aggregation
import java.time.ZoneId

/**
 * Aggregates one metric over a time range.
 *
 * @param aggregation **Aggregation** the metric
 * @param range **TimeRange** time range to aggregate
 * @param origins **Set<DataOrigin>** only records written by these apps. All apps by default
 * @return **Aggregate** the value over [range]; its value is null when there is no data
 * @throws HealthConnectException.NotAvailable when the aggregated type needs a feature the device lacks
 * @throws HealthConnectException.NotAuthorized when read permission is missing
 */
public suspend fun HealthConnectReader.aggregate(
    aggregation: Aggregation,
    range: TimeRange,
    origins: Set<DataOrigin> = emptySet(),
): Aggregate {
    client.checkFeature(aggregation.healthType)
    val result =
        clientCall {
            client.aggregate(
                AggregateRequest(
                    metrics = setOf(aggregation.metric),
                    timeRangeFilter = range.asTimeRangeFilter,
                    dataOriginFilter = origins.asOriginal,
                ),
            )
        }
    return Aggregate(aggregation, range.startTimestamp, range.endTimestamp, result.harmonize(aggregation))
}

/**
 * Aggregates metrics over a time range, grouped into buckets.
 *
 * [TimeGroup.Duration] buckets have a fixed physical length; [TimeGroup.Period] buckets are calendar periods in
 * their zone, over [range] converted to local time there.
 *
 * @param aggregations **Set<Aggregation>** the metrics, at least one
 * @param range **TimeRange** time range to aggregate
 * @param group **TimeGroup** the buckets
 * @param origins **Set<DataOrigin>** only records written by these apps. All apps by default
 * @return **List<Aggregate>** one aggregate per bucket and metric, buckets in time order; empty without data
 * @throws HealthConnectException.InvalidValue when [aggregations] is empty
 * @throws HealthConnectException.NotAvailable when an aggregated type needs a feature the device lacks
 * @throws HealthConnectException.NotAuthorized when read permission is missing
 */
public suspend fun HealthConnectReader.aggregate(
    aggregations: Set<Aggregation>,
    range: TimeRange,
    group: TimeGroup,
    origins: Set<DataOrigin> = emptySet(),
): List<Aggregate> {
    if (aggregations.isEmpty()) throw HealthConnectException.InvalidValue("Invalid aggregations: empty")
    aggregations.forEach { client.checkFeature(it.healthType) }
    val metrics = aggregations.mapTo(mutableSetOf()) { it.metric }
    return when (group) {
        is TimeGroup.Duration -> {
            val buckets =
                clientCall {
                    client.aggregateGroupByDuration(
                        AggregateGroupByDurationRequest(
                            metrics = metrics,
                            timeRangeFilter = range.asTimeRangeFilter,
                            timeRangeSlicer = group.durationMillis.asDuration,
                            dataOriginFilter = origins.asOriginal,
                        ),
                    )
                }
            buckets.flatMap { it.result.aggregates(aggregations, it.startTime.epochMillis, it.endTime.epochMillis) }
        }
        is TimeGroup.Period -> {
            val zoneId = ZoneId.of(group.zoneId)
            val buckets =
                clientCall {
                    client.aggregateGroupByPeriod(
                        AggregateGroupByPeriodRequest(
                            metrics = metrics,
                            timeRangeFilter = range.asLocalTimeRangeFilter(zoneId),
                            timeRangeSlicer = group.asPeriod,
                            dataOriginFilter = origins.asOriginal,
                        ),
                    )
                }
            buckets.flatMap {
                it.result.aggregates(
                    aggregations,
                    it.startTime
                        .atZone(zoneId)
                        .toInstant()
                        .epochMillis,
                    it.endTime
                        .atZone(zoneId)
                        .toInstant()
                        .epochMillis,
                )
            }
        }
    }
}

private fun AggregationResult.aggregates(
    aggregations: Set<Aggregation>,
    startTimestamp: Long,
    endTimestamp: Long,
): List<Aggregate> = aggregations.map { Aggregate(it, startTimestamp, endTimestamp, harmonize(it)) }
