package com.kvs.healthconnectreporter.service

import android.os.RemoteException
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.aggregate.AggregateMetric
import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.SkinTemperatureRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.response.ReadRecordResponse
import androidx.health.connect.client.time.TimeRangeFilter
import com.kvs.healthconnectreporter.END
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.MID
import com.kvs.healthconnectreporter.START
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.metric
import com.kvs.healthconnectreporter.model.Aggregate
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Ordering
import com.kvs.healthconnectreporter.model.TimeGroup
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.type.Aggregation
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.TimeGroupUnit
import com.kvs.healthconnectreporter.response
import com.kvs.healthconnectreporter.skinTemperature
import com.kvs.healthconnectreporter.steps
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame
import androidx.health.connect.client.records.metadata.DataOrigin as OriginalDataOrigin

class HealthConnectReaderTest {
    private val client = mockk<HealthConnectClient>()
    private val sut = HealthConnectReader(client)
    private val range = TimeRange(START, END)
    private val record = steps.asOriginal as StepsRecord
    private val filter = TimeRangeFilter.between(Instant.ofEpochMilli(START), Instant.ofEpochMilli(END))

    private fun request(
        pageSize: Int,
        pageToken: String? = null,
        ascending: Boolean = false,
        origins: Set<OriginalDataOrigin> = emptySet(),
    ) = ReadRecordsRequest(StepsRecord::class, filter, origins, ascending, pageSize, pageToken)

    @Test
    fun `read converts records and follows pages`() =
        runTest {
            coEvery { client.readRecords(request(5000, origins = setOf(OriginalDataOrigin("com.example")))) } returns
                response(listOf(record), "next")
            coEvery {
                client.readRecords(request(5000, "next", origins = setOf(OriginalDataOrigin("com.example"))))
            } returns response(listOf(record))

            val samples = sut.read(HealthType.STEPS, range, origins = setOf(DataOrigin("com.example")))

            assertEquals(listOf(steps, steps), samples)
        }

    @Test
    fun `read stops at the limit`() =
        runTest {
            coEvery { client.readRecords(request(3, ascending = true)) } returns
                response(listOf(record, record), "next")
            coEvery { client.readRecords(request(1, "next", ascending = true)) } returns
                response(listOf(record), "more")

            val samples = sut.read(HealthType.STEPS, range, Ordering.ASCENDING, limit = 3)

            assertEquals(3, samples.size)
            coVerify(exactly = 2) { client.readRecords(any<ReadRecordsRequest<StepsRecord>>()) }
        }

    @Test
    fun `read pages a large limit in pages of 5000`() =
        runTest {
            coEvery { client.readRecords(request(5000)) } returns response(listOf(record))

            assertEquals(listOf(steps), sut.read(HealthType.STEPS, range, limit = 10_000))
        }

    @Test
    fun `read with a limit below 1 never calls the client`() =
        runTest {
            val error =
                assertFailsWith<HealthConnectException.InvalidValue> { sut.read(HealthType.STEPS, range, limit = 0) }

            assertEquals("Invalid limit: 0", error.message)
            coVerify(exactly = 0) { client.readRecords(any<ReadRecordsRequest<StepsRecord>>()) }
        }

    @Test
    fun `read page returns the next page token`() =
        runTest {
            coEvery { client.readRecords(request(1000, "token")) } returns response(listOf(record), "next")

            val page = sut.readPage(HealthType.STEPS, range, pageToken = "token")

            assertEquals(listOf(steps), page.items)
            assertEquals("next", page.nextPageToken)
        }

    @Test
    fun `read page size outside 1 to 5000 never calls the client`() =
        runTest {
            listOf(0, 5001).forEach {
                val error =
                    assertFailsWith<HealthConnectException.InvalidValue> {
                        sut.readPage(HealthType.STEPS, range, pageSize = it)
                    }
                assertEquals("Invalid pageSize: $it", error.message)
            }
            coVerify(exactly = 0) { client.readRecords(any<ReadRecordsRequest<StepsRecord>>()) }
        }

    @Test
    fun `feature gated type needs its feature`() =
        runTest {
            every {
                client.features.getFeatureStatus(HealthConnectFeatures.FEATURE_SKIN_TEMPERATURE)
            } returns HealthConnectFeatures.FEATURE_STATUS_UNAVAILABLE

            val error =
                assertFailsWith<HealthConnectException.NotAvailable> { sut.read(HealthType.SKIN_TEMPERATURE, range) }

            assertEquals("skin_temperature needs Health Connect feature SKIN_TEMPERATURE", error.message)
            coVerify(exactly = 0) { client.readRecords(any<ReadRecordsRequest<SkinTemperatureRecord>>()) }
        }

    @Test
    fun `feature gated type reads when its feature is available`() =
        runTest {
            every {
                client.features.getFeatureStatus(HealthConnectFeatures.FEATURE_SKIN_TEMPERATURE)
            } returns HealthConnectFeatures.FEATURE_STATUS_AVAILABLE
            coEvery { client.readRecords(any<ReadRecordsRequest<SkinTemperatureRecord>>()) } returns
                response(listOf(skinTemperature.asOriginal as SkinTemperatureRecord))

            assertEquals(listOf(skinTemperature), sut.read(HealthType.SKIN_TEMPERATURE, range))
        }

    @Test
    fun `missing permission is not authorized`() =
        runTest {
            val cause = SecurityException("READ_STEPS")
            coEvery { client.readRecords(any<ReadRecordsRequest<StepsRecord>>()) } throws cause

            val error = assertFailsWith<HealthConnectException.NotAuthorized> { sut.read(HealthType.STEPS, range) }

            assertSame(cause, error.cause)
        }

    @Test
    fun `read by id returns the record`() =
        runTest {
            coEvery { client.readRecord(StepsRecord::class, "id-1") } returns ReadRecordResponse(record)

            assertEquals(steps, sut.read(HealthType.STEPS, "id-1"))
        }

    @Test
    fun `read by id wraps remote errors as platform errors`() =
        runTest {
            coEvery { client.readRecord(StepsRecord::class, "id-1") } throws RemoteException("not found")

            assertFailsWith<HealthConnectException.Platform> { sut.read(HealthType.STEPS, "id-1") }
        }

    @Test
    fun `aggregate returns the value in its unit`() =
        runTest {
            val result = mockk<AggregationResult>()
            every { result[StepsRecord.COUNT_TOTAL] } returns 1200L
            coEvery { client.aggregate(AggregateRequest(setOf(StepsRecord.COUNT_TOTAL), filter)) } returns result

            val aggregate = sut.aggregate(Aggregation.STEPS_COUNT_TOTAL, range)

            assertEquals(Aggregate(Aggregation.STEPS_COUNT_TOTAL, START, END, 1200.0, "count"), aggregate)
        }

    @Test
    fun `aggregate without data has no value`() =
        runTest {
            val result = mockk<AggregationResult>()
            every { result[any<AggregateMetric<Any>>()] } returns null
            coEvery { client.aggregate(any()) } returns result

            assertNull(sut.aggregate(Aggregation.WEIGHT_WEIGHT_AVG, range).value)
        }

    @Test
    fun `aggregate grouped by duration returns one aggregate per bucket and metric`() =
        runTest {
            val result = mockk<AggregationResult>()
            every { result[StepsRecord.COUNT_TOTAL] } returns 10L
            every { result[DistanceRecord.DISTANCE_TOTAL] } returns null
            val bucket = { start: Long, end: Long ->
                AggregationResultGroupedByDuration(
                    result,
                    Instant.ofEpochMilli(start),
                    Instant.ofEpochMilli(end),
                    ZoneOffset.UTC,
                )
            }
            val aggregations = setOf(Aggregation.STEPS_COUNT_TOTAL, Aggregation.DISTANCE_DISTANCE_TOTAL)
            coEvery {
                client.aggregateGroupByDuration(
                    AggregateGroupByDurationRequest(
                        aggregations.map { it.metric }.toSet(),
                        filter,
                        Duration.ofMinutes(30),
                    ),
                )
            } returns listOf(bucket(START, MID), bucket(MID, END))

            val aggregates = sut.aggregate(aggregations, range, TimeGroup.Duration(1_800_000))

            assertEquals(
                listOf(
                    Aggregate(Aggregation.STEPS_COUNT_TOTAL, START, MID, 10.0),
                    Aggregate(Aggregation.DISTANCE_DISTANCE_TOTAL, START, MID, null),
                    Aggregate(Aggregation.STEPS_COUNT_TOTAL, MID, END, 10.0),
                    Aggregate(Aggregation.DISTANCE_DISTANCE_TOTAL, MID, END, null),
                ),
                aggregates,
            )
        }

    @Test
    fun `aggregate grouped by period uses local time in the zone`() =
        runTest {
            val zone = ZoneId.of("Europe/Berlin")
            val start = LocalDateTime.of(2026, 10, 1, 0, 0)
            val end = LocalDateTime.of(2026, 10, 3, 0, 0)
            val local =
                TimeRange(start.atZone(zone).toInstant().toEpochMilli(), end.atZone(zone).toInstant().toEpochMilli())
            val result = mockk<AggregationResult>()
            every { result[StepsRecord.COUNT_TOTAL] } returns 5L
            coEvery {
                client.aggregateGroupByPeriod(
                    AggregateGroupByPeriodRequest(
                        setOf(StepsRecord.COUNT_TOTAL),
                        TimeRangeFilter.between(start, end),
                        Period.ofDays(2),
                    ),
                )
            } returns listOf(AggregationResultGroupedByPeriod(result, start, end))

            val aggregates =
                sut.aggregate(
                    setOf(Aggregation.STEPS_COUNT_TOTAL),
                    local,
                    TimeGroup.Period(TimeGroupUnit.DAILY, 2, zone.id),
                )

            assertEquals(
                listOf(Aggregate(Aggregation.STEPS_COUNT_TOTAL, local.startTimestamp, local.endTimestamp, 5.0)),
                aggregates,
            )
        }

    @Test
    fun `aggregate of no metrics never calls the client`() =
        runTest {
            val error =
                assertFailsWith<HealthConnectException.InvalidValue> {
                    sut.aggregate(emptySet(), range, TimeGroup.Duration(1))
                }

            assertEquals("Invalid aggregations: empty", error.message)
        }

    @Test
    fun `aggregate of a feature gated type needs its feature`() =
        runTest {
            every { client.features.getFeatureStatus(any()) } returns HealthConnectFeatures.FEATURE_STATUS_UNAVAILABLE

            assertFailsWith<HealthConnectException.NotAvailable> {
                sut.aggregate(Aggregation.SKIN_TEMPERATURE_TEMPERATURE_DELTA_AVG, range)
            }
            assertFailsWith<HealthConnectException.NotAvailable> {
                sut.aggregate(
                    setOf(Aggregation.SKIN_TEMPERATURE_TEMPERATURE_DELTA_MAX),
                    range,
                    TimeGroup.Duration(1),
                )
            }
        }
}
