package com.kvs.healthconnectreporter.payload

import com.kvs.healthconnectreporter.END
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.MID
import com.kvs.healthconnectreporter.START
import com.kvs.healthconnectreporter.allSamples
import com.kvs.healthconnectreporter.asMap
import com.kvs.healthconnectreporter.exerciseSession
import com.kvs.healthconnectreporter.factory
import com.kvs.healthconnectreporter.heartRate
import com.kvs.healthconnectreporter.model.payload.CyclingPedalingCadence
import com.kvs.healthconnectreporter.model.payload.ExerciseSession
import com.kvs.healthconnectreporter.model.payload.HeartRate
import com.kvs.healthconnectreporter.model.payload.Nutrition
import com.kvs.healthconnectreporter.model.payload.Power
import com.kvs.healthconnectreporter.model.payload.Sample
import com.kvs.healthconnectreporter.model.payload.SkinTemperature
import com.kvs.healthconnectreporter.model.payload.SleepSession
import com.kvs.healthconnectreporter.model.payload.Speed
import com.kvs.healthconnectreporter.model.payload.StepsCadence
import com.kvs.healthconnectreporter.model.payload.WritableSample
import com.kvs.healthconnectreporter.model.type.ExerciseSegmentType
import com.kvs.healthconnectreporter.model.type.ExerciseType
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.SleepStageType
import com.kvs.healthconnectreporter.sleepSession
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SampleTest {
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `every type has exactly one fixture`() {
        assertEquals(HealthType.entries, allSamples.map { it.healthType })
    }

    @Test
    fun `create then encode then decode`() {
        allSamples.forEach { sut ->
            val decoded = json.decodeFromString(Sample.serializer(), sut.json)

            assertEquals(sut, decoded, sut.healthType.identifier)
        }
    }

    @Test
    fun `writable samples decode as writable samples`() {
        allSamples.forEach { sut ->
            assertEquals(sut, json.decodeFromString(WritableSample.serializer(), sut.json), sut.healthType.identifier)
        }
    }

    @Test
    fun `samples without optional values decode`() {
        val sut =
            listOf(
                exerciseSession.copy(harmonized = ExerciseSession.Harmonized(ExerciseType.RUNNING)),
                SkinTemperature(START, END, SkinTemperature.Harmonized(emptyList())),
                Nutrition(START, END, Nutrition.Harmonized()),
            )

        sut.forEach { assertEquals(it, json.decodeFromString(Sample.serializer(), it.json)) }
    }

    @Test
    fun `encoded sample carries its type as discriminator`() {
        allSamples.forEach { sut ->
            assertEquals(sut.healthType.identifier, sut.json.asMap()["type"], sut.healthType.identifier)
        }
    }

    @Test
    fun `list of mixed samples round trips`() {
        val serializer = ListSerializer(Sample.serializer())

        val encoded = json.encodeToString(serializer, allSamples)

        assertEquals(allSamples, json.decodeFromString(serializer, encoded))
    }

    @Test
    fun `create from a flutter map`() {
        allSamples.forEach { sut ->
            assertEquals(sut, sut.healthType.factory.make(sut.json.asMap()), sut.healthType.identifier)
        }
    }

    @Test
    fun `decode from json`() {
        allSamples.forEach { sut ->
            assertEquals(sut, sut.healthType.factory.decode(sut.json), sut.healthType.identifier)
        }
    }

    @Test
    fun `missing key is an invalid value`() {
        allSamples.forEach { sut ->
            val map = sut.json.asMap() - "startTimestamp"

            assertFailsWith<HealthConnectException.InvalidValue>(sut.healthType.identifier) {
                sut.healthType.factory.make(map)
            }
        }
    }

    @Test
    fun `mistyped key is an invalid value`() {
        allSamples.forEach { sut ->
            val map = sut.json.asMap() + ("startTimestamp" to "yesterday")

            assertFailsWith<HealthConnectException.InvalidValue>(sut.healthType.identifier) {
                sut.healthType.factory.make(map)
            }
        }
    }

    @Test
    fun `end before start is an invalid value`() {
        allSamples.forEach { sut ->
            val map = sut.json.asMap() + ("endTimestamp" to START - 1)

            val error =
                assertFailsWith<HealthConnectException.InvalidValue>(sut.healthType.identifier) {
                    sut.healthType.factory.make(map)
                }
            assertEquals(
                "Invalid ${sut.healthType.identifier}: endTimestamp ${START - 1} is before startTimestamp $START",
                error.message,
            )
        }
    }

    @Test
    fun `series values outside the interval are invalid values`() {
        val outside = END + 1
        val sut =
            listOf(
                { HeartRate(START, MID, HeartRate.Harmonized(listOf(HeartRate.Series(outside, 70)))) },
                { HeartRate(START, END, HeartRate.Harmonized(listOf(HeartRate.Series(START - 1, 70)))) },
                { Power(START, END, Power.Harmonized(listOf(Power.Series(outside, 1.0)))) },
                { Speed(START, END, Speed.Harmonized(listOf(Speed.Series(outside, 1.0)))) },
                { StepsCadence(START, END, StepsCadence.Harmonized(listOf(StepsCadence.Series(outside, 1.0)))) },
                {
                    CyclingPedalingCadence(
                        START,
                        END,
                        CyclingPedalingCadence.Harmonized(listOf(CyclingPedalingCadence.Series(outside, 1.0))),
                    )
                },
                {
                    SkinTemperature(
                        START,
                        END,
                        SkinTemperature.Harmonized(listOf(SkinTemperature.Delta(outside, 1.0))),
                    )
                },
                {
                    sleepSession.copy(
                        harmonized =
                            sleepSession.harmonized.copy(
                                stages = listOf(SleepSession.Stage(MID, outside, SleepStageType.DEEP)),
                            ),
                    )
                },
                {
                    exerciseSession.copy(
                        harmonized =
                            exerciseSession.harmonized.copy(
                                segments =
                                    listOf(ExerciseSession.Segment(MID, outside, ExerciseSegmentType.RUNNING)),
                            ),
                    )
                },
                {
                    exerciseSession.copy(
                        harmonized = exerciseSession.harmonized.copy(laps = listOf(ExerciseSession.Lap(MID, outside))),
                    )
                },
                {
                    exerciseSession.copy(
                        harmonized =
                            exerciseSession.harmonized.copy(
                                route = ExerciseSession.Route(listOf(ExerciseSession.Location(outside, 0.0, 0.0))),
                            ),
                    )
                },
            )

        sut.forEach { assertFailsWith<HealthConnectException.InvalidValue> { it() } }
    }

    @Test
    fun `nested interval ending before its start is an invalid value`() {
        val sut =
            listOf(
                { SleepSession.Stage(MID, START, SleepStageType.DEEP) },
                { ExerciseSession.Segment(MID, START, ExerciseSegmentType.RUNNING) },
                { ExerciseSession.Lap(MID, START) },
            )

        sut.forEach {
            val error = assertFailsWith<HealthConnectException.InvalidValue> { it() }
            assertEquals("Invalid interval: end $START is before start $MID", error.message)
        }
    }

    @Test
    fun `collect skips invalid maps`() {
        val valid = heartRate.json.asMap()
        val invalid = valid - "harmonized"

        val sut = HeartRate.collect(listOf(valid, invalid))

        assertEquals(listOf(heartRate), sut)
    }

    @Test
    fun `create from a map with every plain value kind`() {
        val sut =
            ExerciseSession.make(
                mapOf(
                    "startTimestamp" to JsonPrimitive(START),
                    "endTimestamp" to END,
                    "id" to null,
                    "harmonized" to
                        mapOf(
                            "exerciseType" to ExerciseType.RUNNING,
                            "isRouteConsentRequired" to false,
                            "segments" to
                                arrayOf(
                                    mapOf(
                                        "startTimestamp" to START,
                                        "endTimestamp" to MID,
                                        "segmentType" to "RUNNING",
                                    ),
                                ),
                            "laps" to
                                listOf(
                                    mapOf("startTimestamp" to START, "endTimestamp" to MID, "length" to 400.5),
                                ),
                        ),
                ),
            )

        assertEquals(ExerciseType.RUNNING, sut.harmonized.exerciseType)
        assertEquals(1, sut.harmonized.segments.size)
        assertEquals(
            400.5,
            sut.harmonized.laps
                .single()
                .length,
        )
    }

    @Test
    fun `create from a map with an unsupported value is an invalid value`() {
        val error =
            assertFailsWith<HealthConnectException.InvalidValue> {
                HeartRate.make(mapOf("startTimestamp" to Any()))
            }

        assertEquals(true, error.message?.startsWith("Unsupported map value"))
    }

    @Test
    fun `decode invalid json is an invalid value`() {
        val error = assertFailsWith<HealthConnectException.InvalidValue> { HeartRate.decode("{") }

        assertEquals("Invalid json: {", error.message)
    }
}
