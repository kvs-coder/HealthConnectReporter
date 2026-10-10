package com.kvs.healthconnectreporter.payload

import androidx.health.connect.client.records.ExerciseCompletionGoal
import androidx.health.connect.client.records.ExercisePerformanceTarget
import androidx.health.connect.client.records.PlannedExerciseSessionRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.metadata.Metadata
import com.kvs.healthconnectreporter.END
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.MID
import com.kvs.healthconnectreporter.OFFSET
import com.kvs.healthconnectreporter.START
import com.kvs.healthconnectreporter.allSamples
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asSample
import com.kvs.healthconnectreporter.decorator.harmonized
import com.kvs.healthconnectreporter.decorator.metadata
import com.kvs.healthconnectreporter.decorator.recordClass
import com.kvs.healthconnectreporter.decorator.sample
import com.kvs.healthconnectreporter.decorator.withClientRecordId
import com.kvs.healthconnectreporter.device
import com.kvs.healthconnectreporter.exerciseSession
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.payload.ExerciseSession
import com.kvs.healthconnectreporter.model.payload.Nutrition
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession.CompletionGoal
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession.PerformanceTarget
import com.kvs.healthconnectreporter.model.payload.SkinTemperature
import com.kvs.healthconnectreporter.model.payload.Steps
import com.kvs.healthconnectreporter.model.payload.WritableSample
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import com.kvs.healthconnectreporter.plannedExerciseSession
import com.kvs.healthconnectreporter.steps
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import androidx.health.connect.client.records.metadata.DataOrigin as OriginalDataOrigin

class ConversionTest {
    @Test
    fun `every sample converts to its record and back`() {
        allSamples.filterIsInstance<WritableSample>().forEach { sut ->
            val record = sut.asOriginal

            assertEquals(sut.healthType.recordClass, record::class, sut.healthType.identifier)
            assertEquals(sut, sut.healthType.sample(record), sut.healthType.identifier)
        }
    }

    @Test
    fun `samples with every optional value unset convert and back`() {
        val sut =
            listOf(
                Nutrition(START, END, Nutrition.Harmonized(), recordingMethod = RecordingMethod.MANUAL_ENTRY),
                SkinTemperature(
                    START,
                    END,
                    SkinTemperature.Harmonized(emptyList()),
                    recordingMethod = RecordingMethod.MANUAL_ENTRY,
                ),
                exerciseSession.copy(
                    harmonized =
                        exerciseSession.harmonized.copy(
                            laps = listOf(ExerciseSession.Lap(START, MID)),
                            route = ExerciseSession.Route(listOf(ExerciseSession.Location(MID, 1.0, 2.0))),
                        ),
                ),
                exerciseSession.copy(harmonized = exerciseSession.harmonized.copy(route = null)),
            )

        sut.forEach { assertEquals(it, it.asOriginal.asSample) }
    }

    @Test
    fun `every completion goal and performance target converts and back`() {
        val goals =
            listOf(
                CompletionGoal.Distance(1000.0),
                CompletionGoal.DistanceAndDuration(1000.0, 300_000),
                CompletionGoal.Steps(500),
                CompletionGoal.Duration(60_000),
                CompletionGoal.Repetitions(10),
                CompletionGoal.TotalCaloriesBurned(100.0),
                CompletionGoal.ActiveCaloriesBurned(50.0),
                CompletionGoal.ManualCompletion,
                CompletionGoal.Unknown,
            )
        val targets =
            listOf(
                PerformanceTarget.Power(100.0, 200.0),
                PerformanceTarget.Speed(2.0, 3.0),
                PerformanceTarget.Cadence(80.0, 90.0),
                PerformanceTarget.HeartRate(120.0, 140.0),
                PerformanceTarget.Weight(20.0),
                PerformanceTarget.RateOfPerceivedExertion(7),
                PerformanceTarget.Amrap,
                PerformanceTarget.Unknown,
            )

        goals.forEach { assertEquals(it, it.asOriginal.harmonized) }
        targets.forEach { assertEquals(it, it.asOriginal.harmonized) }
        assertEquals(CompletionGoal.Unknown, ExerciseCompletionGoal.UnknownGoal.harmonized)
        assertEquals(PerformanceTarget.Unknown, ExercisePerformanceTarget.UnknownTarget.harmonized)
    }

    @Test
    fun `planned session without explicit time is planned for the day of its start`() {
        listOf(OFFSET, null).forEach { offset ->
            val sut =
                plannedExerciseSession.copy(
                    startZoneOffsetSeconds = offset,
                    harmonized = plannedExerciseSession.harmonized.copy(hasExplicitTime = false),
                )

            val record = sut.asOriginal as PlannedExerciseSessionRecord

            assertFalse(record.hasExplicitTime)
            assertEquals(LocalTime.NOON, record.startTime.atOffset(record.startZoneOffset).toLocalTime())
            assertEquals(END - START, record.endTime.toEpochMilli() - record.startTime.toEpochMilli())
            assertFalse((record.asSample as PlannedExerciseSession).harmonized.hasExplicitTime)
        }
    }

    @Test
    fun `metadata uses the factory of the recording method`() {
        RecordingMethod.entries.forEach { method ->
            val withId = steps.copy(id = "id-1", recordingMethod = method, device = device).metadata()
            val withClientId = steps.copy(clientRecordVersion = 3, recordingMethod = method, device = device).metadata()
            val plain = steps.copy(clientRecordId = null, recordingMethod = method, device = device).metadata()

            assertEquals(method, Steps.from(record(withId)).recordingMethod, method.name)
            assertEquals("id-1", withId.id, method.name)
            assertEquals("client-steps", withClientId.clientRecordId, method.name)
            assertEquals(3, withClientId.clientRecordVersion, method.name)
            assertNull(plain.clientRecordId, method.name)
            assertEquals(device, Steps.from(record(plain)).device, method.name)
        }
    }

    @Test
    fun `metadata without a recording method is a manual entry without a device`() {
        val sut = steps.copy(clientRecordId = null, recordingMethod = null, device = null).metadata()

        assertEquals(Metadata.RECORDING_METHOD_MANUAL_ENTRY, sut.recordingMethod)
        assertNull(sut.device)
    }

    @Test
    fun `recorded samples need a device`() {
        listOf(RecordingMethod.ACTIVELY_RECORDED, RecordingMethod.AUTOMATICALLY_RECORDED).forEach { method ->
            val error =
                assertFailsWith<HealthConnectException.InvalidValue> {
                    steps.copy(recordingMethod = method, device = null).metadata()
                }
            assertEquals("Invalid steps: $method needs a device", error.message)
        }
    }

    @Test
    fun `stored metadata maps origin, modification time and version`() {
        val stored = mockk<Metadata>()
        every { stored.id } returns "id-1"
        every { stored.clientRecordId } returns "client-1"
        every { stored.clientRecordVersion } returns 7
        every { stored.recordingMethod } returns Metadata.RECORDING_METHOD_AUTOMATICALLY_RECORDED
        every { stored.device } returns null
        every { stored.dataOrigin } returns OriginalDataOrigin("com.example")
        every { stored.lastModifiedTime } returns Instant.ofEpochMilli(END)

        val sut = Steps.from(record(stored))

        assertEquals("id-1", sut.id)
        assertEquals(7, sut.clientRecordVersion)
        assertEquals(RecordingMethod.AUTOMATICALLY_RECORDED, sut.recordingMethod)
        assertEquals(DataOrigin("com.example"), sut.dataOrigin)
        assertEquals(END, sut.lastModifiedTimestamp)
    }

    @Test
    fun `unstored metadata has no id, origin, modification time or version`() {
        val sut = Steps.from(record(Metadata.manualEntry()))

        assertNull(sut.id)
        assertNull(sut.clientRecordId)
        assertNull(sut.clientRecordVersion)
        assertNull(sut.dataOrigin)
        assertNull(sut.lastModifiedTimestamp)
    }

    @Test
    fun `unknown recording method constant maps to unknown`() {
        val stored = mockk<Metadata>(relaxed = true)
        every { stored.recordingMethod } returns Int.MAX_VALUE
        every { stored.dataOrigin } returns OriginalDataOrigin("")
        every { stored.lastModifiedTime } returns Instant.EPOCH

        assertEquals(RecordingMethod.UNKNOWN, Steps.from(record(stored)).recordingMethod)
    }

    @Test
    fun `with client record id copies every sample type`() {
        allSamples.filterIsInstance<WritableSample>().forEach {
            assertEquals("new", it.withClientRecordId("new").clientRecordId, it.healthType.identifier)
        }
    }

    @Test
    fun `record of another type is an invalid type`() {
        val error = assertFailsWith<HealthConnectException.InvalidType> { HealthType.WEIGHT.sample(steps.asOriginal) }

        assertEquals("Invalid record for weight: StepsRecord", error.message)
    }

    @Test
    fun `unknown record is an invalid type`() {
        assertFailsWith<HealthConnectException.InvalidType> { mockk<Record>().asSample }
    }

    @Test
    fun `zone offsets convert to seconds`() {
        val sut = steps.copy(startZoneOffsetSeconds = OFFSET, endZoneOffsetSeconds = null).asOriginal as StepsRecord

        assertEquals(ZoneOffset.ofTotalSeconds(OFFSET), sut.startZoneOffset)
        assertNull(sut.endZoneOffset)
        assertTrue(Steps.from(sut).endZoneOffsetSeconds == null)
    }

    private fun record(metadata: Metadata) =
        StepsRecord(
            startTime = Instant.ofEpochMilli(START),
            startZoneOffset = null,
            endTime = Instant.ofEpochMilli(END),
            endZoneOffset = null,
            count = 10,
            metadata = metadata,
        )
}
