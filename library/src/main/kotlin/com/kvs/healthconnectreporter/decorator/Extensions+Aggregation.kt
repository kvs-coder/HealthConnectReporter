package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.aggregate.AggregateMetric
import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SkinTemperatureRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.records.WheelchairPushesRecord
import com.kvs.healthconnectreporter.model.type.Aggregation

internal val Aggregation.metric: AggregateMetric<Any>
    get() =
        when (this) {
            Aggregation.ACTIVE_CALORIES_BURNED_ACTIVE_CALORIES_TOTAL -> ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL
            Aggregation.BASAL_METABOLIC_RATE_BASAL_CALORIES_TOTAL -> BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL
            Aggregation.BLOOD_PRESSURE_SYSTOLIC_AVG -> BloodPressureRecord.SYSTOLIC_AVG
            Aggregation.BLOOD_PRESSURE_SYSTOLIC_MIN -> BloodPressureRecord.SYSTOLIC_MIN
            Aggregation.BLOOD_PRESSURE_SYSTOLIC_MAX -> BloodPressureRecord.SYSTOLIC_MAX
            Aggregation.BLOOD_PRESSURE_DIASTOLIC_AVG -> BloodPressureRecord.DIASTOLIC_AVG
            Aggregation.BLOOD_PRESSURE_DIASTOLIC_MIN -> BloodPressureRecord.DIASTOLIC_MIN
            Aggregation.BLOOD_PRESSURE_DIASTOLIC_MAX -> BloodPressureRecord.DIASTOLIC_MAX
            Aggregation.CYCLING_PEDALING_CADENCE_RPM_AVG -> CyclingPedalingCadenceRecord.RPM_AVG
            Aggregation.CYCLING_PEDALING_CADENCE_RPM_MIN -> CyclingPedalingCadenceRecord.RPM_MIN
            Aggregation.CYCLING_PEDALING_CADENCE_RPM_MAX -> CyclingPedalingCadenceRecord.RPM_MAX
            Aggregation.DISTANCE_DISTANCE_TOTAL -> DistanceRecord.DISTANCE_TOTAL
            Aggregation.ELEVATION_GAINED_ELEVATION_GAINED_TOTAL -> ElevationGainedRecord.ELEVATION_GAINED_TOTAL
            Aggregation.EXERCISE_SESSION_EXERCISE_DURATION_TOTAL -> ExerciseSessionRecord.EXERCISE_DURATION_TOTAL
            Aggregation.FLOORS_CLIMBED_FLOORS_CLIMBED_TOTAL -> FloorsClimbedRecord.FLOORS_CLIMBED_TOTAL
            Aggregation.HEART_RATE_BPM_AVG -> HeartRateRecord.BPM_AVG
            Aggregation.HEART_RATE_BPM_MIN -> HeartRateRecord.BPM_MIN
            Aggregation.HEART_RATE_BPM_MAX -> HeartRateRecord.BPM_MAX
            Aggregation.HEART_RATE_MEASUREMENTS_COUNT -> HeartRateRecord.MEASUREMENTS_COUNT
            Aggregation.HEIGHT_HEIGHT_AVG -> HeightRecord.HEIGHT_AVG
            Aggregation.HEIGHT_HEIGHT_MIN -> HeightRecord.HEIGHT_MIN
            Aggregation.HEIGHT_HEIGHT_MAX -> HeightRecord.HEIGHT_MAX
            Aggregation.HYDRATION_VOLUME_TOTAL -> HydrationRecord.VOLUME_TOTAL
            Aggregation.NUTRITION_BIOTIN_TOTAL -> NutritionRecord.BIOTIN_TOTAL
            Aggregation.NUTRITION_CAFFEINE_TOTAL -> NutritionRecord.CAFFEINE_TOTAL
            Aggregation.NUTRITION_CALCIUM_TOTAL -> NutritionRecord.CALCIUM_TOTAL
            Aggregation.NUTRITION_ENERGY_TOTAL -> NutritionRecord.ENERGY_TOTAL
            Aggregation.NUTRITION_ENERGY_FROM_FAT_TOTAL -> NutritionRecord.ENERGY_FROM_FAT_TOTAL
            Aggregation.NUTRITION_CHLORIDE_TOTAL -> NutritionRecord.CHLORIDE_TOTAL
            Aggregation.NUTRITION_CHOLESTEROL_TOTAL -> NutritionRecord.CHOLESTEROL_TOTAL
            Aggregation.NUTRITION_CHROMIUM_TOTAL -> NutritionRecord.CHROMIUM_TOTAL
            Aggregation.NUTRITION_COPPER_TOTAL -> NutritionRecord.COPPER_TOTAL
            Aggregation.NUTRITION_DIETARY_FIBER_TOTAL -> NutritionRecord.DIETARY_FIBER_TOTAL
            Aggregation.NUTRITION_FOLATE_TOTAL -> NutritionRecord.FOLATE_TOTAL
            Aggregation.NUTRITION_FOLIC_ACID_TOTAL -> NutritionRecord.FOLIC_ACID_TOTAL
            Aggregation.NUTRITION_IODINE_TOTAL -> NutritionRecord.IODINE_TOTAL
            Aggregation.NUTRITION_IRON_TOTAL -> NutritionRecord.IRON_TOTAL
            Aggregation.NUTRITION_MAGNESIUM_TOTAL -> NutritionRecord.MAGNESIUM_TOTAL
            Aggregation.NUTRITION_MANGANESE_TOTAL -> NutritionRecord.MANGANESE_TOTAL
            Aggregation.NUTRITION_MOLYBDENUM_TOTAL -> NutritionRecord.MOLYBDENUM_TOTAL
            Aggregation.NUTRITION_MONOUNSATURATED_FAT_TOTAL -> NutritionRecord.MONOUNSATURATED_FAT_TOTAL
            Aggregation.NUTRITION_NIACIN_TOTAL -> NutritionRecord.NIACIN_TOTAL
            Aggregation.NUTRITION_PANTOTHENIC_ACID_TOTAL -> NutritionRecord.PANTOTHENIC_ACID_TOTAL
            Aggregation.NUTRITION_PHOSPHORUS_TOTAL -> NutritionRecord.PHOSPHORUS_TOTAL
            Aggregation.NUTRITION_POLYUNSATURATED_FAT_TOTAL -> NutritionRecord.POLYUNSATURATED_FAT_TOTAL
            Aggregation.NUTRITION_POTASSIUM_TOTAL -> NutritionRecord.POTASSIUM_TOTAL
            Aggregation.NUTRITION_PROTEIN_TOTAL -> NutritionRecord.PROTEIN_TOTAL
            Aggregation.NUTRITION_RIBOFLAVIN_TOTAL -> NutritionRecord.RIBOFLAVIN_TOTAL
            Aggregation.NUTRITION_SATURATED_FAT_TOTAL -> NutritionRecord.SATURATED_FAT_TOTAL
            Aggregation.NUTRITION_SELENIUM_TOTAL -> NutritionRecord.SELENIUM_TOTAL
            Aggregation.NUTRITION_SODIUM_TOTAL -> NutritionRecord.SODIUM_TOTAL
            Aggregation.NUTRITION_SUGAR_TOTAL -> NutritionRecord.SUGAR_TOTAL
            Aggregation.NUTRITION_THIAMIN_TOTAL -> NutritionRecord.THIAMIN_TOTAL
            Aggregation.NUTRITION_TOTAL_CARBOHYDRATE_TOTAL -> NutritionRecord.TOTAL_CARBOHYDRATE_TOTAL
            Aggregation.NUTRITION_TOTAL_FAT_TOTAL -> NutritionRecord.TOTAL_FAT_TOTAL
            Aggregation.NUTRITION_TRANS_FAT_TOTAL -> NutritionRecord.TRANS_FAT_TOTAL
            Aggregation.NUTRITION_UNSATURATED_FAT_TOTAL -> NutritionRecord.UNSATURATED_FAT_TOTAL
            Aggregation.NUTRITION_VITAMIN_A_TOTAL -> NutritionRecord.VITAMIN_A_TOTAL
            Aggregation.NUTRITION_VITAMIN_B12_TOTAL -> NutritionRecord.VITAMIN_B12_TOTAL
            Aggregation.NUTRITION_VITAMIN_B6_TOTAL -> NutritionRecord.VITAMIN_B6_TOTAL
            Aggregation.NUTRITION_VITAMIN_C_TOTAL -> NutritionRecord.VITAMIN_C_TOTAL
            Aggregation.NUTRITION_VITAMIN_D_TOTAL -> NutritionRecord.VITAMIN_D_TOTAL
            Aggregation.NUTRITION_VITAMIN_E_TOTAL -> NutritionRecord.VITAMIN_E_TOTAL
            Aggregation.NUTRITION_VITAMIN_K_TOTAL -> NutritionRecord.VITAMIN_K_TOTAL
            Aggregation.NUTRITION_ZINC_TOTAL -> NutritionRecord.ZINC_TOTAL
            Aggregation.POWER_POWER_AVG -> PowerRecord.POWER_AVG
            Aggregation.POWER_POWER_MIN -> PowerRecord.POWER_MIN
            Aggregation.POWER_POWER_MAX -> PowerRecord.POWER_MAX
            Aggregation.RESTING_HEART_RATE_BPM_AVG -> RestingHeartRateRecord.BPM_AVG
            Aggregation.RESTING_HEART_RATE_BPM_MIN -> RestingHeartRateRecord.BPM_MIN
            Aggregation.RESTING_HEART_RATE_BPM_MAX -> RestingHeartRateRecord.BPM_MAX
            Aggregation.SKIN_TEMPERATURE_TEMPERATURE_DELTA_AVG -> SkinTemperatureRecord.TEMPERATURE_DELTA_AVG
            Aggregation.SKIN_TEMPERATURE_TEMPERATURE_DELTA_MIN -> SkinTemperatureRecord.TEMPERATURE_DELTA_MIN
            Aggregation.SKIN_TEMPERATURE_TEMPERATURE_DELTA_MAX -> SkinTemperatureRecord.TEMPERATURE_DELTA_MAX
            Aggregation.SLEEP_SESSION_SLEEP_DURATION_TOTAL -> SleepSessionRecord.SLEEP_DURATION_TOTAL
            Aggregation.SPEED_SPEED_AVG -> SpeedRecord.SPEED_AVG
            Aggregation.SPEED_SPEED_MIN -> SpeedRecord.SPEED_MIN
            Aggregation.SPEED_SPEED_MAX -> SpeedRecord.SPEED_MAX
            Aggregation.STEPS_CADENCE_RATE_AVG -> StepsCadenceRecord.RATE_AVG
            Aggregation.STEPS_CADENCE_RATE_MIN -> StepsCadenceRecord.RATE_MIN
            Aggregation.STEPS_CADENCE_RATE_MAX -> StepsCadenceRecord.RATE_MAX
            Aggregation.STEPS_COUNT_TOTAL -> StepsRecord.COUNT_TOTAL
            Aggregation.TOTAL_CALORIES_BURNED_ENERGY_TOTAL -> TotalCaloriesBurnedRecord.ENERGY_TOTAL
            Aggregation.WEIGHT_WEIGHT_AVG -> WeightRecord.WEIGHT_AVG
            Aggregation.WEIGHT_WEIGHT_MIN -> WeightRecord.WEIGHT_MIN
            Aggregation.WEIGHT_WEIGHT_MAX -> WeightRecord.WEIGHT_MAX
            Aggregation.WHEELCHAIR_PUSHES_COUNT_TOTAL -> WheelchairPushesRecord.COUNT_TOTAL
        }

/** **Double?** the aggregated value in [Aggregation.unit]; null when Health Connect has no data */
internal fun AggregationResult.harmonize(aggregation: Aggregation): Double? = this[aggregation.metric]?.canonical
