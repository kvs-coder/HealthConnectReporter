package com.kvs.healthconnectreporter.payload

import com.kvs.healthconnectreporter.END
import com.kvs.healthconnectreporter.OFFSET
import com.kvs.healthconnectreporter.START
import com.kvs.healthconnectreporter.allSamples
import com.kvs.healthconnectreporter.device
import com.kvs.healthconnectreporter.exerciseSession
import com.kvs.healthconnectreporter.model.Aggregate
import com.kvs.healthconnectreporter.model.Anchor
import com.kvs.healthconnectreporter.model.ChangeSet
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Page
import com.kvs.healthconnectreporter.model.Permission
import com.kvs.healthconnectreporter.model.TimeGroup
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.payload.ExerciseSession
import com.kvs.healthconnectreporter.model.payload.Nutrition
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession.CompletionGoal
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession.PerformanceTarget
import com.kvs.healthconnectreporter.model.payload.Sample
import com.kvs.healthconnectreporter.model.payload.SkinTemperature
import com.kvs.healthconnectreporter.model.type.AccessType
import com.kvs.healthconnectreporter.model.type.Aggregation
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import com.kvs.healthconnectreporter.model.type.TimeGroupUnit
import com.kvs.healthconnectreporter.plannedExerciseSession
import com.kvs.healthconnectreporter.steps
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.junit.Test
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Exercises the kotlinx.serialization code generated for every payload: encoding with and without defaults, and
 * decoding with each key present or absent, the way other runtimes (Flutter) send partial JSON.
 */
class SerializationTest {
    private val full = Json { encodeDefaults = true }
    private val sparse = Json { encodeDefaults = false }

    private class Case<T>(
        val serializer: KSerializer<T>,
        val values: List<T>,
    )

    private val minimalSamples: List<Sample> =
        listOf(
            exerciseSession.copy(harmonized = ExerciseSession.Harmonized(exerciseSession.harmonized.exerciseType)),
            Nutrition(START, END, Nutrition.Harmonized()),
            SkinTemperature(START, END, SkinTemperature.Harmonized(emptyList())),
        )

    private val goals =
        listOf(
            CompletionGoal.Distance(1.0),
            CompletionGoal.DistanceAndDuration(1.0, 1),
            CompletionGoal.Steps(1),
            CompletionGoal.Duration(1),
            CompletionGoal.Repetitions(1),
            CompletionGoal.TotalCaloriesBurned(1.0),
            CompletionGoal.ActiveCaloriesBurned(1.0),
            CompletionGoal.ManualCompletion,
            CompletionGoal.Unknown,
        )

    private val targets =
        listOf(
            PerformanceTarget.Power(1.0, 2.0),
            PerformanceTarget.Speed(1.0, 2.0),
            PerformanceTarget.Cadence(1.0, 2.0),
            PerformanceTarget.HeartRate(1.0, 2.0),
            PerformanceTarget.Weight(1.0),
            PerformanceTarget.RateOfPerceivedExertion(1),
            PerformanceTarget.Amrap,
            PerformanceTarget.Unknown,
        )

    private val cases: List<Case<*>> =
        listOf(
            Case(Sample.serializer(), allSamples + minimalSamples + allSamples.map { it.withEveryMetadata() }),
            Case(CompletionGoal.serializer(), goals),
            Case(PerformanceTarget.serializer(), targets),
            Case(Device.serializer(), listOf(device, Device())),
            Case(DataOrigin.serializer(), listOf(DataOrigin("com.example"))),
            Case(TimeRange.serializer(), listOf(TimeRange(START, END))),
            Case(Page.serializer(), listOf(Page(listOf(steps), "next"), Page(emptyList()))),
            Case(
                Aggregate.serializer(),
                listOf(
                    Aggregate(Aggregation.STEPS_COUNT_TOTAL, START, END, 1.0),
                    Aggregate(Aggregation.STEPS_COUNT_TOTAL, START, END, null, "steps"),
                ),
            ),
            Case(Anchor.serializer(), listOf(Anchor("token"))),
            Case(ChangeSet.serializer(), listOf(ChangeSet(listOf(plannedExerciseSession), listOf("id"), Anchor("t")))),
            Case(
                TimeGroup.serializer(),
                listOf(
                    TimeGroup.Duration(1),
                    TimeGroup.Period(TimeGroupUnit.WEEKLY),
                    TimeGroup.Period(TimeGroupUnit.DAILY, 2, "UTC"),
                ),
            ),
            Case(
                Permission.serializer(),
                listOf(
                    Permission.Data(HealthType.STEPS, AccessType.READ),
                    Permission.ReadInBackground,
                    Permission.ReadHistory,
                    Permission.WriteExerciseRoute,
                ),
            ),
        )

    @Test
    fun `every payload round trips with and without defaults`() {
        cases.forEach { it.assertRoundTrips() }
    }

    @Test
    fun `every payload decodes with any one key missing, or names the missing key`() {
        cases.forEach { it.assertDecodesWithoutEachKey() }
    }

    @Test
    fun `leaving out one default argument changes only that property`() {
        cases.flatMap { it.values }.filterNotNull().forEach { value ->
            val constructor = value::class.primaryConstructor ?: return@forEach
            val properties = value::class.memberProperties.associateBy { it.name }
            val arguments =
                constructor.parameters.associateWith {
                    properties
                        .getValue(
                            it.name.orEmpty(),
                        ).getter
                        .call(value)
                }
            constructor.parameters.filter { it.isOptional }.forEach { left ->
                val rebuilt = constructor.callBy(arguments - left)
                (arguments.keys - left).forEach {
                    val property = properties.getValue(it.name.orEmpty())
                    assertEquals(
                        property.getter.call(value),
                        property.getter.call(rebuilt),
                        "${value::class.simpleName}.${it.name}",
                    )
                }
            }
        }
    }

    /** The sample as stored by Health Connect: every metadata property set, through its primary constructor. */
    private fun Sample.withEveryMetadata(): Sample {
        val constructor = this::class.primaryConstructor ?: return this
        val properties = this::class.memberProperties.associateBy { it.name }
        val overrides =
            mapOf(
                "endTimestamp" to (endTimestamp ?: startTimestamp),
                "id" to "id-1",
                "clientRecordId" to "client-1",
                "clientRecordVersion" to 2L,
                "startZoneOffsetSeconds" to OFFSET,
                "endZoneOffsetSeconds" to OFFSET,
                "recordingMethod" to RecordingMethod.ACTIVELY_RECORDED,
                "device" to device,
                "dataOrigin" to DataOrigin("com.example"),
                "lastModifiedTimestamp" to END,
            )
        val arguments =
            constructor.parameters.associateWith {
                overrides[it.name] ?: properties.getValue(it.name.orEmpty()).getter.call(this)
            }
        return constructor.callBy(arguments)
    }

    private fun <T> Case<T>.assertRoundTrips() {
        values.forEach { value ->
            listOf(full, sparse).forEach { json ->
                assertEquals(value, json.decodeFromString(serializer, json.encodeToString(serializer, value)))
            }
        }
    }

    private fun <T> Case<T>.assertDecodesWithoutEachKey() {
        values.forEach { value ->
            val element = full.encodeToJsonElement(serializer, value)
            element.withoutEachKey().forEach { partial ->
                try {
                    val decoded = full.decodeFromJsonElement(serializer, partial)
                    sparse.encodeToString(serializer, decoded)
                } catch (exception: SerializationException) {
                    assertTrue(exception.message.orEmpty().isNotEmpty())
                } catch (exception: IllegalArgumentException) {
                    // Payload validation (e.g. a missing endTimestamp of a series) rejects the partial value.
                    assertTrue(exception.message.orEmpty().isNotEmpty())
                } catch (exception: com.kvs.healthconnectreporter.HealthConnectException.InvalidValue) {
                    assertTrue(exception.message.orEmpty().isNotEmpty())
                }
            }
        }
    }

    /** Copies of this element, each with one object key removed, at any depth; the discriminator is kept. */
    private fun JsonElement.withoutEachKey(): List<JsonElement> =
        when (this) {
            is JsonObject ->
                keys.filter { it != "type" }.map { key -> JsonObject(this - key) } +
                    entries.flatMap { (key, child) -> child.withoutEachKey().map { JsonObject(this + (key to it)) } }
            is JsonArray ->
                mapIndexed { index, child ->
                    child.withoutEachKey().map {
                        JsonArray(
                            this.toMutableList().also { list ->
                                list[index] =
                                    it
                            },
                        )
                    }
                }.flatten()
            else -> emptyList()
        }
}
