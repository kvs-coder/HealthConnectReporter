package com.kvs.healthconnectreporter.service

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.changes.Change
import androidx.health.connect.client.changes.DeletionChange
import androidx.health.connect.client.changes.UpsertionChange
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ChangesTokenRequest
import androidx.health.connect.client.response.ChangesResponse
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.model.Anchor
import com.kvs.healthconnectreporter.model.ChangeSet
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.steps
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import androidx.health.connect.client.records.metadata.DataOrigin as OriginalDataOrigin

class HealthConnectObserverTest {
    private val client = mockk<HealthConnectClient>()
    private val sut = HealthConnectObserver(client)
    private val upsertion = UpsertionChange(steps.asOriginal)

    private fun changes(
        next: String,
        changes: List<Change> = emptyList(),
        hasMore: Boolean = false,
        expired: Boolean = false,
    ) = ChangesResponse(changes, next, hasMore, expired)

    @Test
    fun `anchor wraps the changes token of the types`() =
        runTest {
            coEvery {
                client.getChangesToken(
                    ChangesTokenRequest(
                        setOf(StepsRecord::class, HeartRateRecord::class),
                        setOf(OriginalDataOrigin("com.example")),
                    ),
                )
            } returns "token-1"

            val anchor = sut.anchor(setOf(HealthType.STEPS, HealthType.HEART_RATE), setOf(DataOrigin("com.example")))

            assertEquals(Anchor("token-1"), anchor)
        }

    @Test
    fun `anchor of no types never calls the client`() =
        runTest {
            val error = assertFailsWith<HealthConnectException.InvalidValue> { sut.anchor(emptySet()) }

            assertEquals("Invalid anchor types: empty", error.message)
            coVerify(exactly = 0) { client.getChangesToken(any()) }
        }

    @Test
    fun `anchor of a feature gated type needs its feature`() =
        runTest {
            every { client.features.getFeatureStatus(any()) } returns HealthConnectFeatures.FEATURE_STATUS_UNAVAILABLE

            assertFailsWith<HealthConnectException.NotAvailable> {
                sut.anchor(
                    setOf(HealthType.PLANNED_EXERCISE_SESSION),
                )
            }
        }

    @Test
    fun `changes follow has more and advance the anchor`() =
        runTest {
            coEvery { client.getChanges("token-1") } returns changes("token-2", listOf(upsertion), hasMore = true)
            coEvery { client.getChanges("token-2") } returns changes("token-3", listOf(DeletionChange("id-9")))

            val changeSet = sut.changes(Anchor("token-1"))

            assertEquals(ChangeSet(listOf(steps), listOf("id-9"), Anchor("token-3")), changeSet)
        }

    @Test
    fun `expired anchor must be read again`() =
        runTest {
            coEvery { client.getChanges("old") } returns changes("old", expired = true)

            assertFailsWith<HealthConnectException.AnchorExpired> { sut.changes(Anchor("old")) }
        }

    @Test
    fun `observe emits only non-empty change sets on every interval`() =
        runTest {
            coEvery { client.getChanges("token-1") } returns changes("token-2")
            coEvery { client.getChanges("token-2") } returns changes("token-3", listOf(upsertion))
            coEvery { client.getChanges("token-3") } returns changes("token-3")

            val changeSet = sut.observe(Anchor("token-1")).first()

            assertEquals(Anchor("token-3"), changeSet.anchor)
            assertEquals(1.minutes.inWholeMilliseconds, currentTime)
        }

    @Test
    fun `observe keeps polling from the last anchor`() =
        runTest {
            coEvery { client.getChanges("token-1") } returns changes("token-2", listOf(upsertion))
            coEvery { client.getChanges("token-2") } returns changes("token-3", listOf(DeletionChange("id-1")))

            val changeSets = sut.observe(Anchor("token-1"), interval = 5.minutes).take(2).toList()

            assertEquals(listOf(Anchor("token-2"), Anchor("token-3")), changeSets.map { it.anchor })
            assertEquals(5.minutes.inWholeMilliseconds, currentTime)
        }

    @Test
    fun `observe needs a positive interval`() {
        listOf(Duration.ZERO, (-1).minutes).forEach {
            assertFailsWith<HealthConnectException.InvalidValue> { sut.observe(Anchor("token"), it) }
        }
    }

    @Test
    fun `change set payloads round trip through json`() {
        val changeSet = ChangeSet(listOf(steps), listOf("id-1"), Anchor("token"))

        assertEquals(changeSet, ChangeSet.decode(changeSet.json))
        assertEquals(Anchor("token"), Anchor.decode(Anchor("token").json))
        assertTrue(ChangeSet(emptyList(), emptyList(), Anchor("token")).isEmpty)
    }
}
