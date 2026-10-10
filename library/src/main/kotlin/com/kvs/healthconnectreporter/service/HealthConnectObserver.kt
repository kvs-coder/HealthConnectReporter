package com.kvs.healthconnectreporter.service

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.changes.DeletionChange
import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.request.ChangesTokenRequest
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asSample
import com.kvs.healthconnectreporter.decorator.clientCall
import com.kvs.healthconnectreporter.decorator.recordClass
import com.kvs.healthconnectreporter.model.Anchor
import com.kvs.healthconnectreporter.model.ChangeSet
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.payload.Sample
import com.kvs.healthconnectreporter.model.type.HealthType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * **HealthConnectObserver** class for reading Health Connect changes.
 *
 * Reading changes needs read permission for the observed types; in the background it also needs
 * the read-in-background permission.
 */
public class HealthConnectObserver internal constructor(
    private val client: HealthConnectClient,
) {
    /**
     * Creates an anchor at the current end of the change history.
     *
     * @param types **Set<HealthType>** the types to observe, at least one
     * @param origins **Set<DataOrigin>** only changes written by these apps. All apps by default
     * @return **Anchor** pass it to [changes] or [observe]
     * @throws HealthConnectException.InvalidValue when [types] is empty
     * @throws HealthConnectException.NotAvailable when a type needs a Health Connect feature the device lacks
     * @throws HealthConnectException.NotAuthorized when read permission is missing
     */
    public suspend fun anchor(
        types: Set<HealthType>,
        origins: Set<DataOrigin> = emptySet(),
    ): Anchor {
        if (types.isEmpty()) throw HealthConnectException.InvalidValue("Invalid anchor types: empty")
        types.forEach { client.checkFeature(it) }
        val token =
            clientCall {
                client.getChangesToken(
                    ChangesTokenRequest(
                        recordTypes = types.mapTo(mutableSetOf()) { it.recordClass },
                        dataOriginFilters = origins.asOriginal,
                    ),
                )
            }
        return Anchor(token)
    }

    /**
     * Reads every change since [anchor].
     *
     * @param anchor **Anchor** from [anchor] or a previous **ChangeSet**
     * @return **ChangeSet** the upserted samples, deleted ids and the next anchor; empty when nothing changed
     * @throws HealthConnectException.AnchorExpired when [anchor] is older than Health Connect's 30-day history
     * @throws HealthConnectException.NotAuthorized when read permission is missing
     */
    public suspend fun changes(anchor: Anchor): ChangeSet {
        val upserted = mutableListOf<Sample>()
        val deletedIds = mutableListOf<String>()
        var token = anchor.token
        do {
            val response = clientCall { client.getChanges(token) }
            if (response.changesTokenExpired) {
                throw HealthConnectException.AnchorExpired("Health Connect changes anchor expired; read the data again")
            }
            upserted += response.changes.filterIsInstance<UpsertionChange>().map { it.record.asSample }
            deletedIds += response.changes.filterIsInstance<DeletionChange>().map { it.recordId }
            token = response.nextChangesToken
        } while (response.hasMore)
        return ChangeSet(upserted, deletedIds, Anchor(token))
    }

    /**
     * Polls for changes since [anchor], every [interval], while collected.
     *
     * The flow is cold: each collector polls on its own, first right away. It emits only non-empty change sets and
     * ends with an exception from [changes]. Store the last emitted **ChangeSet.anchor** to resume.
     *
     * @param anchor **Anchor** from [anchor] or a previous **ChangeSet**
     * @param interval **Duration** time between polls, positive. 1 minute by default
     * @return **Flow<ChangeSet>** the non-empty change sets
     * @throws HealthConnectException.InvalidValue when [interval] is not positive
     */
    public fun observe(
        anchor: Anchor,
        interval: Duration = 1.minutes,
    ): Flow<ChangeSet> {
        if (!interval.isPositive()) throw HealthConnectException.InvalidValue("Invalid interval: $interval")
        return flow {
            var current = anchor
            while (true) {
                val changeSet = changes(current)
                current = changeSet.anchor
                if (!changeSet.isEmpty) emit(changeSet)
                delay(interval)
            }
        }
    }
}
