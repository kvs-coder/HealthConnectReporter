@file:OptIn(ExperimentalMindfulnessSessionApi::class)

package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.feature.ExperimentalMindfulnessSessionApi
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.BodyWaterMassRecord
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.MenstruationFlowRecord
import androidx.health.connect.client.records.MenstruationPeriodRecord
import androidx.health.connect.client.records.MindfulnessSessionRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.OvulationTestRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.PlannedExerciseSessionRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SexualActivityRecord
import androidx.health.connect.client.records.SkinTemperatureRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.Vo2MaxRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.WheelchairPushesRecord
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.model.payload.ActiveCaloriesBurned
import com.kvs.healthconnectreporter.model.payload.BasalBodyTemperature
import com.kvs.healthconnectreporter.model.payload.BasalMetabolicRate
import com.kvs.healthconnectreporter.model.payload.BloodGlucose
import com.kvs.healthconnectreporter.model.payload.BloodPressure
import com.kvs.healthconnectreporter.model.payload.BodyFat
import com.kvs.healthconnectreporter.model.payload.BodyTemperature
import com.kvs.healthconnectreporter.model.payload.BodyWaterMass
import com.kvs.healthconnectreporter.model.payload.BoneMass
import com.kvs.healthconnectreporter.model.payload.CervicalMucus
import com.kvs.healthconnectreporter.model.payload.CyclingPedalingCadence
import com.kvs.healthconnectreporter.model.payload.Distance
import com.kvs.healthconnectreporter.model.payload.ElevationGained
import com.kvs.healthconnectreporter.model.payload.ExerciseSession
import com.kvs.healthconnectreporter.model.payload.FloorsClimbed
import com.kvs.healthconnectreporter.model.payload.HeartRate
import com.kvs.healthconnectreporter.model.payload.HeartRateVariabilityRmssd
import com.kvs.healthconnectreporter.model.payload.Height
import com.kvs.healthconnectreporter.model.payload.Hydration
import com.kvs.healthconnectreporter.model.payload.IntermenstrualBleeding
import com.kvs.healthconnectreporter.model.payload.LeanBodyMass
import com.kvs.healthconnectreporter.model.payload.MenstruationFlow
import com.kvs.healthconnectreporter.model.payload.MenstruationPeriod
import com.kvs.healthconnectreporter.model.payload.MindfulnessSession
import com.kvs.healthconnectreporter.model.payload.Nutrition
import com.kvs.healthconnectreporter.model.payload.OvulationTest
import com.kvs.healthconnectreporter.model.payload.OxygenSaturation
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession
import com.kvs.healthconnectreporter.model.payload.Power
import com.kvs.healthconnectreporter.model.payload.RespiratoryRate
import com.kvs.healthconnectreporter.model.payload.RestingHeartRate
import com.kvs.healthconnectreporter.model.payload.Sample
import com.kvs.healthconnectreporter.model.payload.SexualActivity
import com.kvs.healthconnectreporter.model.payload.SkinTemperature
import com.kvs.healthconnectreporter.model.payload.SleepSession
import com.kvs.healthconnectreporter.model.payload.Speed
import com.kvs.healthconnectreporter.model.payload.Steps
import com.kvs.healthconnectreporter.model.payload.StepsCadence
import com.kvs.healthconnectreporter.model.payload.TotalCaloriesBurned
import com.kvs.healthconnectreporter.model.payload.Vo2Max
import com.kvs.healthconnectreporter.model.payload.Weight
import com.kvs.healthconnectreporter.model.payload.WheelchairPushes
import com.kvs.healthconnectreporter.model.type.HealthType
import kotlin.reflect.KClass

internal val HealthType.recordClass: KClass<out Record>
    get() =
        when (this) {
            HealthType.ACTIVE_CALORIES_BURNED -> ActiveCaloriesBurnedRecord::class
            HealthType.BASAL_BODY_TEMPERATURE -> BasalBodyTemperatureRecord::class
            HealthType.BASAL_METABOLIC_RATE -> BasalMetabolicRateRecord::class
            HealthType.BLOOD_GLUCOSE -> BloodGlucoseRecord::class
            HealthType.BLOOD_PRESSURE -> BloodPressureRecord::class
            HealthType.BODY_FAT -> BodyFatRecord::class
            HealthType.BODY_TEMPERATURE -> BodyTemperatureRecord::class
            HealthType.BODY_WATER_MASS -> BodyWaterMassRecord::class
            HealthType.BONE_MASS -> BoneMassRecord::class
            HealthType.CERVICAL_MUCUS -> CervicalMucusRecord::class
            HealthType.CYCLING_PEDALING_CADENCE -> CyclingPedalingCadenceRecord::class
            HealthType.DISTANCE -> DistanceRecord::class
            HealthType.ELEVATION_GAINED -> ElevationGainedRecord::class
            HealthType.EXERCISE_SESSION -> ExerciseSessionRecord::class
            HealthType.FLOORS_CLIMBED -> FloorsClimbedRecord::class
            HealthType.HEART_RATE -> HeartRateRecord::class
            HealthType.HEART_RATE_VARIABILITY_RMSSD -> HeartRateVariabilityRmssdRecord::class
            HealthType.HEIGHT -> HeightRecord::class
            HealthType.HYDRATION -> HydrationRecord::class
            HealthType.INTERMENSTRUAL_BLEEDING -> IntermenstrualBleedingRecord::class
            HealthType.LEAN_BODY_MASS -> LeanBodyMassRecord::class
            HealthType.MENSTRUATION_FLOW -> MenstruationFlowRecord::class
            HealthType.MENSTRUATION_PERIOD -> MenstruationPeriodRecord::class
            HealthType.MINDFULNESS_SESSION -> MindfulnessSessionRecord::class
            HealthType.NUTRITION -> NutritionRecord::class
            HealthType.OVULATION_TEST -> OvulationTestRecord::class
            HealthType.OXYGEN_SATURATION -> OxygenSaturationRecord::class
            HealthType.PLANNED_EXERCISE_SESSION -> PlannedExerciseSessionRecord::class
            HealthType.POWER -> PowerRecord::class
            HealthType.RESPIRATORY_RATE -> RespiratoryRateRecord::class
            HealthType.RESTING_HEART_RATE -> RestingHeartRateRecord::class
            HealthType.SEXUAL_ACTIVITY -> SexualActivityRecord::class
            HealthType.SKIN_TEMPERATURE -> SkinTemperatureRecord::class
            HealthType.SLEEP_SESSION -> SleepSessionRecord::class
            HealthType.SPEED -> SpeedRecord::class
            HealthType.STEPS -> StepsRecord::class
            HealthType.STEPS_CADENCE -> StepsCadenceRecord::class
            HealthType.TOTAL_CALORIES_BURNED -> TotalCaloriesBurnedRecord::class
            HealthType.VO2_MAX -> Vo2MaxRecord::class
            HealthType.WEIGHT -> WeightRecord::class
            HealthType.WHEELCHAIR_PUSHES -> WheelchairPushesRecord::class
        }

internal val HealthType.readPermission: String
    get() = HealthPermission.getReadPermission(recordClass)

internal val HealthType.writePermission: String
    get() = HealthPermission.getWritePermission(recordClass)

/** Converts a record of this type into its sample. */
internal fun HealthType.sample(record: Record): Sample {
    if (!recordClass.isInstance(record)) {
        throw HealthConnectException.InvalidType("Invalid record for $identifier: ${record::class.simpleName}")
    }
    return record.asSample
}

internal val Record.asSample: Sample
    get() =
        when (this) {
            is ActiveCaloriesBurnedRecord -> ActiveCaloriesBurned.from(this)
            is BasalBodyTemperatureRecord -> BasalBodyTemperature.from(this)
            is BasalMetabolicRateRecord -> BasalMetabolicRate.from(this)
            is BloodGlucoseRecord -> BloodGlucose.from(this)
            is BloodPressureRecord -> BloodPressure.from(this)
            is BodyFatRecord -> BodyFat.from(this)
            is BodyTemperatureRecord -> BodyTemperature.from(this)
            is BodyWaterMassRecord -> BodyWaterMass.from(this)
            is BoneMassRecord -> BoneMass.from(this)
            is CervicalMucusRecord -> CervicalMucus.from(this)
            is CyclingPedalingCadenceRecord -> CyclingPedalingCadence.from(this)
            is DistanceRecord -> Distance.from(this)
            is ElevationGainedRecord -> ElevationGained.from(this)
            is ExerciseSessionRecord -> ExerciseSession.from(this)
            is FloorsClimbedRecord -> FloorsClimbed.from(this)
            is HeartRateRecord -> HeartRate.from(this)
            is HeartRateVariabilityRmssdRecord -> HeartRateVariabilityRmssd.from(this)
            is HeightRecord -> Height.from(this)
            is HydrationRecord -> Hydration.from(this)
            is IntermenstrualBleedingRecord -> IntermenstrualBleeding.from(this)
            is LeanBodyMassRecord -> LeanBodyMass.from(this)
            is MenstruationFlowRecord -> MenstruationFlow.from(this)
            is MenstruationPeriodRecord -> MenstruationPeriod.from(this)
            is MindfulnessSessionRecord -> MindfulnessSession.from(this)
            is NutritionRecord -> Nutrition.from(this)
            is OvulationTestRecord -> OvulationTest.from(this)
            is OxygenSaturationRecord -> OxygenSaturation.from(this)
            is PlannedExerciseSessionRecord -> PlannedExerciseSession.from(this)
            is PowerRecord -> Power.from(this)
            is RespiratoryRateRecord -> RespiratoryRate.from(this)
            is RestingHeartRateRecord -> RestingHeartRate.from(this)
            is SexualActivityRecord -> SexualActivity.from(this)
            is SkinTemperatureRecord -> SkinTemperature.from(this)
            is SleepSessionRecord -> SleepSession.from(this)
            is SpeedRecord -> Speed.from(this)
            is StepsRecord -> Steps.from(this)
            is StepsCadenceRecord -> StepsCadence.from(this)
            is TotalCaloriesBurnedRecord -> TotalCaloriesBurned.from(this)
            is Vo2MaxRecord -> Vo2Max.from(this)
            is WeightRecord -> Weight.from(this)
            is WheelchairPushesRecord -> WheelchairPushes.from(this)
            else -> throw HealthConnectException.InvalidType("Invalid record: ${this::class.simpleName}")
        }
