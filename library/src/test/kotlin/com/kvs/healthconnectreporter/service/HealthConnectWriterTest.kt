package com.kvs.healthconnectreporter.service

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.response.InsertRecordsResponse
import androidx.health.connect.client.time.TimeRangeFilter
import com.kvs.healthconnectreporter.END
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.START
import com.kvs.healthconnectreporter.heartRate
import com.kvs.healthconnectreporter.mindfulnessSession
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.payload.Steps
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import com.kvs.healthconnectreporter.steps
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class HealthConnectWriterTest {
    private val client = mockk<HealthConnectClient>()
    private var generated = 0
    private val sut = HealthConnectWriter(client) { "generated-${++generated}" }
    private val inserted = mutableListOf<List<Record>>()

    private fun insertReturnsIds() {
        coEvery { client.insertRecords(capture(inserted)) } answers {
            InsertRecordsResponse(inserted.last().indices.map { "id-${inserted.size}-$it" })
        }
    }

    @Test
    fun `insert assigns missing client record ids and returns ids in order`() =
        runTest {
            insertReturnsIds()

            val samples = sut.insert(listOf(steps.copy(clientRecordId = null), heartRate))

            assertEquals(listOf("generated-1", "client-heartRate"), samples.map { it.clientRecordId })
            assertEquals(listOf("id-1-0", "id-1-1"), samples.map { it.id })
            assertEquals(
                listOf("generated-1", "client-heartRate"),
                inserted.single().map { it.metadata.clientRecordId },
            )
        }

    @Test
    fun `insert writes batches of at most 1000 records`() =
        runTest {
            insertReturnsIds()

            val samples = sut.insert(List(2500) { steps })

            assertEquals(listOf(1000, 1000, 500), inserted.map { it.size })
            assertEquals("id-3-499", samples.last().id)
            assertEquals(2500, samples.size)
        }

    @Test
    fun `insert one sample`() =
        runTest {
            insertReturnsIds()

            assertEquals("id-1-0", sut.insert(steps).id)
        }

    @Test
    fun `insert of a feature gated type needs its feature`() =
        runTest {
            every { client.features.getFeatureStatus(any()) } returns HealthConnectFeatures.FEATURE_STATUS_UNAVAILABLE

            val error = assertFailsWith<HealthConnectException.NotAvailable> { sut.insert(mindfulnessSession) }

            assertEquals("mindfulness_session needs Health Connect feature MINDFULNESS_SESSION", error.message)
            coVerify(exactly = 0) { client.insertRecords(any()) }
        }

    @Test
    fun `insert of a value Health Connect rejects is an invalid value`() =
        runTest {
            val error =
                assertFailsWith<HealthConnectException.InvalidValue> {
                    sut.insert(steps.copy(harmonized = Steps.Harmonized(count = 0)))
                }

            assertIs<IllegalArgumentException>(error.cause)
            coVerify(exactly = 0) { client.insertRecords(any()) }
        }

    @Test
    fun `insert of a recorded sample without device is an invalid value`() =
        runTest {
            assertFailsWith<HealthConnectException.InvalidValue> {
                sut.insert(steps.copy(recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED, device = null))
            }
            coVerify(exactly = 0) { client.insertRecords(any()) }
        }

    @Test
    fun `insert without write permission is not authorized`() =
        runTest {
            val cause = SecurityException("WRITE_STEPS")
            coEvery { client.insertRecords(any()) } throws cause

            val error = assertFailsWith<HealthConnectException.NotAuthorized> { sut.insert(steps) }

            assertSame(cause, error.cause)
        }

    @Test
    fun `update upserts by client record id and updates the rest by id`() =
        runTest {
            val upserted = slot<List<Record>>()
            val updated = slot<List<Record>>()
            coEvery { client.insertRecords(capture(upserted)) } returns InsertRecordsResponse(listOf("id-1"))
            coEvery { client.updateRecords(capture(updated)) } returns Unit

            sut.update(
                listOf(
                    steps.copy(clientRecordVersion = 2),
                    steps.copy(id = "id-2", clientRecordId = null),
                ),
            )

            assertEquals(
                "client-steps",
                upserted.captured
                    .single()
                    .metadata.clientRecordId,
            )
            assertEquals(
                2,
                upserted.captured
                    .single()
                    .metadata.clientRecordVersion,
            )
            assertEquals(
                "id-2",
                updated.captured
                    .single()
                    .metadata.id,
            )
        }

    @Test
    fun `update one sample`() =
        runTest {
            coEvery { client.updateRecords(any()) } returns Unit

            sut.update(steps.copy(id = "id-1", clientRecordId = null))

            coVerify(exactly = 1) { client.updateRecords(any()) }
            coVerify(exactly = 0) { client.insertRecords(any()) }
        }

    @Test
    fun `update without identity never calls the client`() =
        runTest {
            val error =
                assertFailsWith<HealthConnectException.InvalidValue> { sut.update(steps.copy(clientRecordId = null)) }

            assertEquals("Invalid steps: update needs an id or a clientRecordId", error.message)
            coVerify(exactly = 0) { client.updateRecords(any()) }
            coVerify(exactly = 0) { client.insertRecords(any()) }
        }

    @Test
    fun `delete by ids and client record ids`() =
        runTest {
            coEvery { client.deleteRecords(StepsRecord::class, listOf("id-1"), listOf("client-1")) } returns Unit

            sut.delete(HealthType.STEPS, listOf("id-1"), listOf("client-1"))

            coVerify(exactly = 1) { client.deleteRecords(StepsRecord::class, listOf("id-1"), listOf("client-1")) }
        }

    @Test
    fun `delete without ids never calls the client`() =
        runTest {
            val error = assertFailsWith<HealthConnectException.InvalidValue> { sut.delete(HealthType.STEPS) }

            assertEquals("Invalid delete of steps: no ids", error.message)
        }

    @Test
    fun `delete a time range`() =
        runTest {
            val filter = TimeRangeFilter.between(Instant.ofEpochMilli(START), Instant.ofEpochMilli(END))
            coEvery { client.deleteRecords(StepsRecord::class, filter) } returns Unit

            sut.delete(HealthType.STEPS, TimeRange(START, END))

            coVerify(exactly = 1) { client.deleteRecords(StepsRecord::class, filter) }
        }

    @Test
    fun `delete a sample by id, else by client record id`() =
        runTest {
            coEvery { client.deleteRecords(StepsRecord::class, any<List<String>>(), any()) } returns Unit

            sut.delete(steps.copy(id = "id-1"))
            sut.delete(steps)

            coVerify(exactly = 1) { client.deleteRecords(StepsRecord::class, listOf("id-1"), emptyList()) }
            coVerify(exactly = 1) { client.deleteRecords(StepsRecord::class, emptyList(), listOf("client-steps")) }
        }

    @Test
    fun `delete a sample without identity is an invalid value`() =
        runTest {
            val error =
                assertFailsWith<HealthConnectException.InvalidValue> { sut.delete(steps.copy(clientRecordId = null)) }

            assertEquals("Invalid steps: delete needs an id or a clientRecordId", error.message)
        }
}
