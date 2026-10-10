@file:OptIn(ExperimentalMindfulnessSessionApi::class)

package com.kvs.healthconnectreporter.type

import androidx.health.connect.client.feature.ExperimentalMindfulnessSessionApi
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.ExerciseSegment
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.PlannedExerciseStep
import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.SkinTemperatureRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import com.kvs.healthconnectreporter.decorator.asBloodPressureMeasurementLocation
import com.kvs.healthconnectreporter.decorator.asBodyPosition
import com.kvs.healthconnectreporter.decorator.asBodyTemperatureMeasurementLocation
import com.kvs.healthconnectreporter.decorator.asCervicalMucusAppearance
import com.kvs.healthconnectreporter.decorator.asCervicalMucusSensation
import com.kvs.healthconnectreporter.decorator.asDeviceType
import com.kvs.healthconnectreporter.decorator.asExercisePhase
import com.kvs.healthconnectreporter.decorator.asExerciseSegmentType
import com.kvs.healthconnectreporter.decorator.asExerciseType
import com.kvs.healthconnectreporter.decorator.asMealType
import com.kvs.healthconnectreporter.decorator.asMenstruationFlowType
import com.kvs.healthconnectreporter.decorator.asMindfulnessSessionType
import com.kvs.healthconnectreporter.decorator.asOvulationTestResult
import com.kvs.healthconnectreporter.decorator.asProtectionUsed
import com.kvs.healthconnectreporter.decorator.asRecordingMethod
import com.kvs.healthconnectreporter.decorator.asRelationToMeal
import com.kvs.healthconnectreporter.decorator.asSkinTemperatureMeasurementLocation
import com.kvs.healthconnectreporter.decorator.asSleepStageType
import com.kvs.healthconnectreporter.decorator.asSpecimenSource
import com.kvs.healthconnectreporter.decorator.asVo2MaxMeasurementMethod
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.model.type.BloodPressureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.BodyPosition
import com.kvs.healthconnectreporter.model.type.BodyTemperatureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.CervicalMucusAppearance
import com.kvs.healthconnectreporter.model.type.CervicalMucusSensation
import com.kvs.healthconnectreporter.model.type.DeviceType
import com.kvs.healthconnectreporter.model.type.ExercisePhase
import com.kvs.healthconnectreporter.model.type.ExerciseSegmentType
import com.kvs.healthconnectreporter.model.type.ExerciseType
import com.kvs.healthconnectreporter.model.type.MealType
import com.kvs.healthconnectreporter.model.type.MenstruationFlowType
import com.kvs.healthconnectreporter.model.type.MindfulnessSessionType
import com.kvs.healthconnectreporter.model.type.OvulationTestResult
import com.kvs.healthconnectreporter.model.type.ProtectionUsed
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import com.kvs.healthconnectreporter.model.type.RelationToMeal
import com.kvs.healthconnectreporter.model.type.SkinTemperatureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.SleepStageType
import com.kvs.healthconnectreporter.model.type.SpecimenSource
import com.kvs.healthconnectreporter.model.type.Vo2MaxMeasurementMethod
import org.junit.Test
import java.lang.reflect.Modifier
import kotlin.test.assertEquals
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation as OriginalLocation
import androidx.health.connect.client.records.MealType as OriginalMealType
import androidx.health.connect.client.records.metadata.Device as OriginalDevice
import androidx.health.connect.client.records.metadata.Metadata as OriginalMetadata

class ValueEnumTest {
    private class Family<T : Enum<T>>(
        val entries: List<T>,
        val owner: Class<*>,
        val prefix: String,
        val constant: (T) -> Int,
        val entry: (Int) -> T,
        val fallback: T,
    )

    private fun <T : Enum<T>> family(
        entries: List<T>,
        owner: Class<*>,
        prefix: String,
        constant: (T) -> Int,
        entry: (Int) -> T,
        fallback: T,
    ) = Family(entries, owner, prefix, constant, entry, fallback)

    private val families =
        listOf(
            family(
                RecordingMethod.entries,
                OriginalMetadata::class.java,
                "RECORDING_METHOD_",
                { it.constant },
                { it.asRecordingMethod },
                RecordingMethod.UNKNOWN,
            ),
            family(
                DeviceType.entries,
                OriginalDevice::class.java,
                "TYPE_",
                { it.constant },
                { it.asDeviceType },
                DeviceType.UNKNOWN,
            ),
            family(
                ExerciseType.entries,
                ExerciseSessionRecord::class.java,
                "EXERCISE_TYPE_",
                { it.constant },
                { it.asExerciseType },
                ExerciseType.OTHER_WORKOUT,
            ),
            family(
                ExerciseSegmentType.entries,
                ExerciseSegment::class.java,
                "EXERCISE_SEGMENT_TYPE_",
                { it.constant },
                { it.asExerciseSegmentType },
                ExerciseSegmentType.UNKNOWN,
            ),
            family(
                ExercisePhase.entries,
                PlannedExerciseStep::class.java,
                "EXERCISE_PHASE_",
                { it.constant },
                { it.asExercisePhase },
                ExercisePhase.UNKNOWN,
            ),
            family(
                SleepStageType.entries,
                SleepSessionRecord::class.java,
                "STAGE_TYPE_",
                { it.constant },
                { it.asSleepStageType },
                SleepStageType.UNKNOWN,
            ),
            family(
                MealType.entries,
                OriginalMealType::class.java,
                "MEAL_TYPE_",
                { it.constant },
                { it.asMealType },
                MealType.UNKNOWN,
            ),
            family(
                MindfulnessSessionType.entries,
                MindfulnessSessionRecord::class.java,
                "MINDFULNESS_SESSION_TYPE_",
                { it.constant },
                { it.asMindfulnessSessionType },
                MindfulnessSessionType.UNKNOWN,
            ),
            family(
                SpecimenSource.entries,
                BloodGlucoseRecord::class.java,
                "SPECIMEN_SOURCE_",
                { it.constant },
                { it.asSpecimenSource },
                SpecimenSource.UNKNOWN,
            ),
            family(
                RelationToMeal.entries,
                BloodGlucoseRecord::class.java,
                "RELATION_TO_MEAL_",
                { it.constant },
                { it.asRelationToMeal },
                RelationToMeal.UNKNOWN,
            ),
            family(
                BodyPosition.entries,
                BloodPressureRecord::class.java,
                "BODY_POSITION_",
                { it.constant },
                { it.asBodyPosition },
                BodyPosition.UNKNOWN,
            ),
            family(
                BloodPressureMeasurementLocation.entries,
                BloodPressureRecord::class.java,
                "MEASUREMENT_LOCATION_",
                { it.constant },
                { it.asBloodPressureMeasurementLocation },
                BloodPressureMeasurementLocation.UNKNOWN,
            ),
            family(
                BodyTemperatureMeasurementLocation.entries,
                OriginalLocation::class.java,
                "MEASUREMENT_LOCATION_",
                { it.constant },
                { it.asBodyTemperatureMeasurementLocation },
                BodyTemperatureMeasurementLocation.UNKNOWN,
            ),
            family(
                SkinTemperatureMeasurementLocation.entries,
                SkinTemperatureRecord::class.java,
                "MEASUREMENT_LOCATION_",
                { it.constant },
                { it.asSkinTemperatureMeasurementLocation },
                SkinTemperatureMeasurementLocation.UNKNOWN,
            ),
            family(
                CervicalMucusAppearance.entries,
                CervicalMucusRecord::class.java,
                "APPEARANCE_",
                { it.constant },
                { it.asCervicalMucusAppearance },
                CervicalMucusAppearance.UNKNOWN,
            ),
            family(
                CervicalMucusSensation.entries,
                CervicalMucusRecord::class.java,
                "SENSATION_",
                { it.constant },
                { it.asCervicalMucusSensation },
                CervicalMucusSensation.UNKNOWN,
            ),
            family(
                MenstruationFlowType.entries,
                MenstruationFlowRecord::class.java,
                "FLOW_",
                { it.constant },
                { it.asMenstruationFlowType },
                MenstruationFlowType.UNKNOWN,
            ),
            family(
                OvulationTestResult.entries,
                OvulationTestRecord::class.java,
                "RESULT_",
                { it.constant },
                { it.asOvulationTestResult },
                OvulationTestResult.INCONCLUSIVE,
            ),
            family(
                ProtectionUsed.entries,
                SexualActivityRecord::class.java,
                "PROTECTION_USED_",
                { it.constant },
                { it.asProtectionUsed },
                ProtectionUsed.UNKNOWN,
            ),
            family(
                Vo2MaxMeasurementMethod.entries,
                Vo2MaxRecord::class.java,
                "MEASUREMENT_METHOD_",
                { it.constant },
                { it.asVo2MaxMeasurementMethod },
                Vo2MaxMeasurementMethod.OTHER,
            ),
        )

    @Test
    fun `every entry maps to its constant by name and back`() {
        families.forEach { it.assertRoundTrip() }
    }

    @Test
    fun `every client constant has an entry`() {
        families.forEach { it.assertComplete() }
    }

    @Test
    fun `unknown constants map to the fallback`() {
        families.forEach { assertEquals(it.fallback, it.entry(Int.MIN_VALUE), it.prefix) }
    }

    private fun <T : Enum<T>> Family<T>.assertRoundTrip() {
        entries.forEach {
            val constant = constant(it)
            assertEquals(owner.getField(prefix + it.name).getInt(null), constant, "$prefix${it.name}")
            assertEquals(it, entry(constant), "$prefix${it.name}")
        }
    }

    private fun Family<*>.assertComplete() {
        val constants =
            owner.fields
                .filter { Modifier.isStatic(it.modifiers) && it.type == Int::class.javaPrimitiveType }
                .map { it.name }
                .filter { it.startsWith(prefix) }
                .toSet()
        assertEquals(constants, entries.map { prefix + it.name }.toSet(), prefix)
    }
}
