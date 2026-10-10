package com.kvs.healthconnectreporter.service

import androidx.health.connect.client.HealthConnectClient
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asTimeRangeFilter
import com.kvs.healthconnectreporter.decorator.clientCall
import com.kvs.healthconnectreporter.decorator.recordClass
import com.kvs.healthconnectreporter.decorator.withClientRecordId
import com.kvs.healthconnectreporter.decorator.withId
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.payload.WritableSample
import com.kvs.healthconnectreporter.model.type.HealthType
import java.util.UUID

/**
 * **HealthConnectWriter** class for Health Connect writing operations.
 *
 * Writing a type needs its write permission; samples without [WritableSample.recordingMethod] are written as
 * manual entries.
 */
public class HealthConnectWriter internal constructor(
    private val client: HealthConnectClient,
    private val makeClientRecordId: () -> String = { UUID.randomUUID().toString() },
) {
    /**
     * Inserts samples, in batches of up to 1000 records per Health Connect call.
     *
     * Samples without a [WritableSample.clientRecordId] get a random one, so they can be updated and deleted later.
     * Inserting a sample whose client record id is already stored replaces it when its
     * [WritableSample.clientRecordVersion] is not lower.
     *
     * @param samples **List<WritableSample>** the samples to insert
     * @return **List<WritableSample>** the samples with their Health Connect [WritableSample.id] and client record id,
     * in input order
     * @throws HealthConnectException.InvalidValue when a sample holds a value Health Connect rejects
     * @throws HealthConnectException.NotAvailable when a type needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when write permission is missing
     */
    public suspend fun insert(samples: List<WritableSample>): List<WritableSample> {
        checkFeatures(samples)
        val identified = samples.map { it.withClientRecordId(it.clientRecordId ?: makeClientRecordId()) }
        return identified.chunked(MAX_BATCH_SIZE).flatMap { batch ->
            val response = clientCall { client.insertRecords(batch.map { it.asOriginal }) }
            batch.zip(response.recordIdsList) { sample, id -> sample.withId(id) }
        }
    }

    /**
     * Inserts one sample.
     *
     * @param sample **WritableSample** the sample to insert
     * @return **WritableSample** the sample with its Health Connect [WritableSample.id] and client record id
     * @throws HealthConnectException.InvalidValue when the sample holds a value Health Connect rejects
     * @throws HealthConnectException.NotAvailable when its type needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when write permission is missing
     */
    public suspend fun insert(sample: WritableSample): WritableSample = insert(listOf(sample)).single()

    /**
     * Updates stored samples.
     *
     * Samples with a [WritableSample.clientRecordId] replace the stored record with that client id when their
     * [WritableSample.clientRecordVersion] is not lower; the others replace the record with their [WritableSample.id].
     *
     * @param samples **List<WritableSample>** the changed samples
     * @throws HealthConnectException.InvalidValue when a sample has neither an id nor a client record id, or holds a
     * value Health Connect rejects
     * @throws HealthConnectException.NotAvailable when a type needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when write permission is missing
     */
    public suspend fun update(samples: List<WritableSample>) {
        samples.forEach {
            if (it.id == null && it.clientRecordId == null) {
                throw HealthConnectException.InvalidValue(
                    "Invalid ${it.healthType.identifier}: update needs an id or a clientRecordId",
                )
            }
        }
        checkFeatures(samples)
        val (upserts, updates) = samples.partition { it.clientRecordId != null }
        upserts.chunked(MAX_BATCH_SIZE).forEach { batch ->
            clientCall { client.insertRecords(batch.map { it.asOriginal }) }
        }
        updates.chunked(MAX_BATCH_SIZE).forEach { batch ->
            clientCall { client.updateRecords(batch.map { it.asOriginal }) }
        }
    }

    /**
     * Updates one stored sample.
     *
     * @param sample **WritableSample** the changed sample
     * @throws HealthConnectException.InvalidValue when the sample has neither an id nor a client record id
     * @throws HealthConnectException.NotAvailable when its type needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when write permission is missing
     */
    public suspend fun update(sample: WritableSample): Unit = update(listOf(sample))

    /**
     * Deletes records by their Health Connect ids and client record ids, all or none.
     *
     * @param type **HealthType** the type of the records
     * @param ids **List<String>** Health Connect record ids. None by default
     * @param clientRecordIds **List<String>** client record ids. None by default
     * @throws HealthConnectException.InvalidValue when both lists are empty
     * @throws HealthConnectException.NotAvailable when [type] needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when write permission is missing
     */
    public suspend fun delete(
        type: HealthType,
        ids: List<String> = emptyList(),
        clientRecordIds: List<String> = emptyList(),
    ) {
        if (ids.isEmpty() && clientRecordIds.isEmpty()) {
            throw HealthConnectException.InvalidValue("Invalid delete of ${type.identifier}: no ids")
        }
        client.checkFeature(type)
        clientCall { client.deleteRecords(type.recordClass, ids, clientRecordIds) }
    }

    /**
     * Deletes the records of one type, written by this app, in a time range.
     *
     * @param type **HealthType** the type of the records
     * @param range **TimeRange** the time range
     * @throws HealthConnectException.NotAvailable when [type] needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when write permission is missing
     */
    public suspend fun delete(
        type: HealthType,
        range: TimeRange,
    ) {
        client.checkFeature(type)
        clientCall { client.deleteRecords(type.recordClass, range.asTimeRangeFilter) }
    }

    /**
     * Deletes a stored sample by its id, else by its client record id.
     *
     * @param sample **WritableSample** the stored sample
     * @throws HealthConnectException.InvalidValue when the sample has neither an id nor a client record id
     * @throws HealthConnectException.NotAvailable when its type needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when write permission is missing
     */
    public suspend fun delete(sample: WritableSample) {
        val id = sample.id
        val clientRecordId = sample.clientRecordId
        when {
            id != null -> delete(sample.healthType, ids = listOf(id))
            clientRecordId != null -> delete(sample.healthType, clientRecordIds = listOf(clientRecordId))
            else -> throw HealthConnectException.InvalidValue(
                "Invalid ${sample.healthType.identifier}: delete needs an id or a clientRecordId",
            )
        }
    }

    private suspend fun checkFeatures(samples: List<WritableSample>) {
        samples.mapTo(mutableSetOf()) { it.healthType }.forEach { client.checkFeature(it) }
    }

    internal companion object {
        const val MAX_BATCH_SIZE = 1000
    }
}
