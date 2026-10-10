package com.kvs.healthconnectreporter.service

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.request.ReadRecordsRequest
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asTimeRangeFilter
import com.kvs.healthconnectreporter.decorator.ascending
import com.kvs.healthconnectreporter.decorator.clientCall
import com.kvs.healthconnectreporter.decorator.recordClass
import com.kvs.healthconnectreporter.decorator.sample
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Ordering
import com.kvs.healthconnectreporter.model.Page
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.payload.Sample
import com.kvs.healthconnectreporter.model.type.HealthType

/**
 * **HealthConnectReader** class for Health Connect reading and aggregation.
 *
 * Without [com.kvs.healthconnectreporter.model.type.Feature.READ_HEALTH_DATA_HISTORY] permission Health Connect
 * returns only data from 30 days before the first permission grant onwards.
 */
public class HealthConnectReader internal constructor(
    internal val client: HealthConnectClient,
) {
    /**
     * Reads the records of one type, following pages until [limit].
     *
     * @param type **HealthType** the type to read
     * @param range **TimeRange** time range to read
     * @param ordering **Ordering** sort by start time. [Ordering.DESCENDING] by default
     * @param limit **Int?** maximum number of samples. No limit by default
     * @param origins **Set<DataOrigin>** only records written by these apps. All apps by default
     * @return **List<Sample>** the stored records, empty when there is no data
     * @throws HealthConnectException.InvalidValue when [limit] is less than 1
     * @throws HealthConnectException.NotAvailable when [type] needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when read permission is missing
     */
    public suspend fun read(
        type: HealthType,
        range: TimeRange,
        ordering: Ordering = Ordering.DESCENDING,
        limit: Int? = null,
        origins: Set<DataOrigin> = emptySet(),
    ): List<Sample> {
        if (limit != null && limit < 1) throw HealthConnectException.InvalidValue("Invalid limit: $limit")
        val samples = mutableListOf<Sample>()
        var pageToken: String? = null
        do {
            val pageSize = minOf(limit?.minus(samples.size) ?: MAX_PAGE_SIZE, MAX_PAGE_SIZE)
            val page = readPage(type, range, pageSize, pageToken, ordering, origins)
            samples += page.items
            pageToken = page.nextPageToken
        } while (pageToken != null && (limit == null || samples.size < limit))
        return samples
    }

    /**
     * Reads one page of records of one type.
     *
     * @param type **HealthType** the type to read
     * @param range **TimeRange** time range to read
     * @param pageSize **Int** maximum number of samples in the page, 1–5000. 1000 by default
     * @param pageToken **String?** [Page.nextPageToken] of the previous page. First page by default
     * @param ordering **Ordering** sort by start time. [Ordering.DESCENDING] by default
     * @param origins **Set<DataOrigin>** only records written by these apps. All apps by default
     * @return **Page** the samples of the page and the token of the next one
     * @throws HealthConnectException.InvalidValue when [pageSize] is outside 1–5000
     * @throws HealthConnectException.NotAvailable when [type] needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when read permission is missing
     */
    public suspend fun readPage(
        type: HealthType,
        range: TimeRange,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        pageToken: String? = null,
        ordering: Ordering = Ordering.DESCENDING,
        origins: Set<DataOrigin> = emptySet(),
    ): Page {
        if (pageSize !in 1..MAX_PAGE_SIZE) throw HealthConnectException.InvalidValue("Invalid pageSize: $pageSize")
        client.checkFeature(type)
        val response =
            clientCall {
                client.readRecords(
                    ReadRecordsRequest(
                        recordType = type.recordClass,
                        timeRangeFilter = range.asTimeRangeFilter,
                        dataOriginFilter = origins.asOriginal,
                        ascendingOrder = ordering.ascending,
                        pageSize = pageSize,
                        pageToken = pageToken,
                    ),
                )
            }
        return Page(response.records.map { type.sample(it) }, response.pageToken)
    }

    /**
     * Reads one record by its Health Connect id.
     *
     * @param type **HealthType** the type of the record
     * @param id **String** the record's [Sample.id]
     * @return **Sample** the stored record
     * @throws HealthConnectException.NotAvailable when [type] needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when read permission is missing
     * @throws HealthConnectException.InvalidValue when no record of [type] has this id
     */
    public suspend fun read(
        type: HealthType,
        id: String,
    ): Sample {
        client.checkFeature(type)
        val response = clientCall { client.readRecord(type.recordClass, id) }
        return type.sample(response.record)
    }

    internal companion object {
        const val DEFAULT_PAGE_SIZE = 1000
        const val MAX_PAGE_SIZE = 5000
    }
}
