package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **Aggregation** one aggregate Health Connect computes over a [healthType], named after its `AggregateMetric`.
 *
 * Health Connect client 1.1.0 can't aggregate mindfulness sessions on Android 14+, so they have no aggregation.
 *
 * Values are reported in the canonical units: durations in milliseconds, masses in kg, energy in kcal.
 *
 * @property healthType **HealthType** the aggregated type
 * @property unit **String** unit of **Aggregate.value**
 */
@Serializable
public enum class Aggregation(
    public val healthType: HealthType,
    public val unit: String,
) {
    /** total active calories burned */
    ACTIVE_CALORIES_BURNED_ACTIVE_CALORIES_TOTAL(HealthType.ACTIVE_CALORIES_BURNED, "kcal"),

    /** total basal calories burned */
    BASAL_METABOLIC_RATE_BASAL_CALORIES_TOTAL(HealthType.BASAL_METABOLIC_RATE, "kcal"),

    /** average systolic pressure */
    BLOOD_PRESSURE_SYSTOLIC_AVG(HealthType.BLOOD_PRESSURE, "mmHg"),

    /** minimum systolic pressure */
    BLOOD_PRESSURE_SYSTOLIC_MIN(HealthType.BLOOD_PRESSURE, "mmHg"),

    /** maximum systolic pressure */
    BLOOD_PRESSURE_SYSTOLIC_MAX(HealthType.BLOOD_PRESSURE, "mmHg"),

    /** average diastolic pressure */
    BLOOD_PRESSURE_DIASTOLIC_AVG(HealthType.BLOOD_PRESSURE, "mmHg"),

    /** minimum diastolic pressure */
    BLOOD_PRESSURE_DIASTOLIC_MIN(HealthType.BLOOD_PRESSURE, "mmHg"),

    /** maximum diastolic pressure */
    BLOOD_PRESSURE_DIASTOLIC_MAX(HealthType.BLOOD_PRESSURE, "mmHg"),

    /** average pedaling cadence */
    CYCLING_PEDALING_CADENCE_RPM_AVG(HealthType.CYCLING_PEDALING_CADENCE, "rpm"),

    /** minimum pedaling cadence */
    CYCLING_PEDALING_CADENCE_RPM_MIN(HealthType.CYCLING_PEDALING_CADENCE, "rpm"),

    /** maximum pedaling cadence */
    CYCLING_PEDALING_CADENCE_RPM_MAX(HealthType.CYCLING_PEDALING_CADENCE, "rpm"),

    /** total distance */
    DISTANCE_DISTANCE_TOTAL(HealthType.DISTANCE, "m"),

    /** total elevation gained */
    ELEVATION_GAINED_ELEVATION_GAINED_TOTAL(HealthType.ELEVATION_GAINED, "m"),

    /** total exercise duration */
    EXERCISE_SESSION_EXERCISE_DURATION_TOTAL(HealthType.EXERCISE_SESSION, "ms"),

    /** total floors climbed */
    FLOORS_CLIMBED_FLOORS_CLIMBED_TOTAL(HealthType.FLOORS_CLIMBED, "floors"),

    /** average heart rate */
    HEART_RATE_BPM_AVG(HealthType.HEART_RATE, "bpm"),

    /** minimum heart rate */
    HEART_RATE_BPM_MIN(HealthType.HEART_RATE, "bpm"),

    /** maximum heart rate */
    HEART_RATE_BPM_MAX(HealthType.HEART_RATE, "bpm"),

    /** number of heart rate measurements */
    HEART_RATE_MEASUREMENTS_COUNT(HealthType.HEART_RATE, "count"),

    /** average height */
    HEIGHT_HEIGHT_AVG(HealthType.HEIGHT, "m"),

    /** minimum height */
    HEIGHT_HEIGHT_MIN(HealthType.HEIGHT, "m"),

    /** maximum height */
    HEIGHT_HEIGHT_MAX(HealthType.HEIGHT, "m"),

    /** total water intake */
    HYDRATION_VOLUME_TOTAL(HealthType.HYDRATION, "L"),

    /** total biotin */
    NUTRITION_BIOTIN_TOTAL(HealthType.NUTRITION, "kg"),

    /** total caffeine */
    NUTRITION_CAFFEINE_TOTAL(HealthType.NUTRITION, "kg"),

    /** total calcium */
    NUTRITION_CALCIUM_TOTAL(HealthType.NUTRITION, "kg"),

    /** total energy */
    NUTRITION_ENERGY_TOTAL(HealthType.NUTRITION, "kcal"),

    /** total energy from fat */
    NUTRITION_ENERGY_FROM_FAT_TOTAL(HealthType.NUTRITION, "kcal"),

    /** total chloride */
    NUTRITION_CHLORIDE_TOTAL(HealthType.NUTRITION, "kg"),

    /** total cholesterol */
    NUTRITION_CHOLESTEROL_TOTAL(HealthType.NUTRITION, "kg"),

    /** total chromium */
    NUTRITION_CHROMIUM_TOTAL(HealthType.NUTRITION, "kg"),

    /** total copper */
    NUTRITION_COPPER_TOTAL(HealthType.NUTRITION, "kg"),

    /** total dietary fiber */
    NUTRITION_DIETARY_FIBER_TOTAL(HealthType.NUTRITION, "kg"),

    /** total folate */
    NUTRITION_FOLATE_TOTAL(HealthType.NUTRITION, "kg"),

    /** total folic acid */
    NUTRITION_FOLIC_ACID_TOTAL(HealthType.NUTRITION, "kg"),

    /** total iodine */
    NUTRITION_IODINE_TOTAL(HealthType.NUTRITION, "kg"),

    /** total iron */
    NUTRITION_IRON_TOTAL(HealthType.NUTRITION, "kg"),

    /** total magnesium */
    NUTRITION_MAGNESIUM_TOTAL(HealthType.NUTRITION, "kg"),

    /** total manganese */
    NUTRITION_MANGANESE_TOTAL(HealthType.NUTRITION, "kg"),

    /** total molybdenum */
    NUTRITION_MOLYBDENUM_TOTAL(HealthType.NUTRITION, "kg"),

    /** total monounsaturated fat */
    NUTRITION_MONOUNSATURATED_FAT_TOTAL(HealthType.NUTRITION, "kg"),

    /** total niacin */
    NUTRITION_NIACIN_TOTAL(HealthType.NUTRITION, "kg"),

    /** total pantothenic acid */
    NUTRITION_PANTOTHENIC_ACID_TOTAL(HealthType.NUTRITION, "kg"),

    /** total phosphorus */
    NUTRITION_PHOSPHORUS_TOTAL(HealthType.NUTRITION, "kg"),

    /** total polyunsaturated fat */
    NUTRITION_POLYUNSATURATED_FAT_TOTAL(HealthType.NUTRITION, "kg"),

    /** total potassium */
    NUTRITION_POTASSIUM_TOTAL(HealthType.NUTRITION, "kg"),

    /** total protein */
    NUTRITION_PROTEIN_TOTAL(HealthType.NUTRITION, "kg"),

    /** total riboflavin */
    NUTRITION_RIBOFLAVIN_TOTAL(HealthType.NUTRITION, "kg"),

    /** total saturated fat */
    NUTRITION_SATURATED_FAT_TOTAL(HealthType.NUTRITION, "kg"),

    /** total selenium */
    NUTRITION_SELENIUM_TOTAL(HealthType.NUTRITION, "kg"),

    /** total sodium */
    NUTRITION_SODIUM_TOTAL(HealthType.NUTRITION, "kg"),

    /** total sugar */
    NUTRITION_SUGAR_TOTAL(HealthType.NUTRITION, "kg"),

    /** total thiamin */
    NUTRITION_THIAMIN_TOTAL(HealthType.NUTRITION, "kg"),

    /** total total carbohydrate */
    NUTRITION_TOTAL_CARBOHYDRATE_TOTAL(HealthType.NUTRITION, "kg"),

    /** total total fat */
    NUTRITION_TOTAL_FAT_TOTAL(HealthType.NUTRITION, "kg"),

    /** total trans fat */
    NUTRITION_TRANS_FAT_TOTAL(HealthType.NUTRITION, "kg"),

    /** total unsaturated fat */
    NUTRITION_UNSATURATED_FAT_TOTAL(HealthType.NUTRITION, "kg"),

    /** total vitamin a */
    NUTRITION_VITAMIN_A_TOTAL(HealthType.NUTRITION, "kg"),

    /** total vitamin b12 */
    NUTRITION_VITAMIN_B12_TOTAL(HealthType.NUTRITION, "kg"),

    /** total vitamin b6 */
    NUTRITION_VITAMIN_B6_TOTAL(HealthType.NUTRITION, "kg"),

    /** total vitamin c */
    NUTRITION_VITAMIN_C_TOTAL(HealthType.NUTRITION, "kg"),

    /** total vitamin d */
    NUTRITION_VITAMIN_D_TOTAL(HealthType.NUTRITION, "kg"),

    /** total vitamin e */
    NUTRITION_VITAMIN_E_TOTAL(HealthType.NUTRITION, "kg"),

    /** total vitamin k */
    NUTRITION_VITAMIN_K_TOTAL(HealthType.NUTRITION, "kg"),

    /** total zinc */
    NUTRITION_ZINC_TOTAL(HealthType.NUTRITION, "kg"),

    /** average power */
    POWER_POWER_AVG(HealthType.POWER, "W"),

    /** minimum power */
    POWER_POWER_MIN(HealthType.POWER, "W"),

    /** maximum power */
    POWER_POWER_MAX(HealthType.POWER, "W"),

    /** average resting heart rate */
    RESTING_HEART_RATE_BPM_AVG(HealthType.RESTING_HEART_RATE, "bpm"),

    /** minimum resting heart rate */
    RESTING_HEART_RATE_BPM_MIN(HealthType.RESTING_HEART_RATE, "bpm"),

    /** maximum resting heart rate */
    RESTING_HEART_RATE_BPM_MAX(HealthType.RESTING_HEART_RATE, "bpm"),

    /** average skin temperature delta from the baseline */
    SKIN_TEMPERATURE_TEMPERATURE_DELTA_AVG(HealthType.SKIN_TEMPERATURE, "°C"),

    /** minimum skin temperature delta from the baseline */
    SKIN_TEMPERATURE_TEMPERATURE_DELTA_MIN(HealthType.SKIN_TEMPERATURE, "°C"),

    /** maximum skin temperature delta from the baseline */
    SKIN_TEMPERATURE_TEMPERATURE_DELTA_MAX(HealthType.SKIN_TEMPERATURE, "°C"),

    /** total sleep duration */
    SLEEP_SESSION_SLEEP_DURATION_TOTAL(HealthType.SLEEP_SESSION, "ms"),

    /** average speed */
    SPEED_SPEED_AVG(HealthType.SPEED, "m/s"),

    /** minimum speed */
    SPEED_SPEED_MIN(HealthType.SPEED, "m/s"),

    /** maximum speed */
    SPEED_SPEED_MAX(HealthType.SPEED, "m/s"),

    /** average steps cadence */
    STEPS_CADENCE_RATE_AVG(HealthType.STEPS_CADENCE, "steps/min"),

    /** minimum steps cadence */
    STEPS_CADENCE_RATE_MIN(HealthType.STEPS_CADENCE, "steps/min"),

    /** maximum steps cadence */
    STEPS_CADENCE_RATE_MAX(HealthType.STEPS_CADENCE, "steps/min"),

    /** total steps */
    STEPS_COUNT_TOTAL(HealthType.STEPS, "count"),

    /** total calories burned */
    TOTAL_CALORIES_BURNED_ENERGY_TOTAL(HealthType.TOTAL_CALORIES_BURNED, "kcal"),

    /** average weight */
    WEIGHT_WEIGHT_AVG(HealthType.WEIGHT, "kg"),

    /** minimum weight */
    WEIGHT_WEIGHT_MIN(HealthType.WEIGHT, "kg"),

    /** maximum weight */
    WEIGHT_WEIGHT_MAX(HealthType.WEIGHT, "kg"),

    /** total wheelchair pushes */
    WHEELCHAIR_PUSHES_COUNT_TOTAL(HealthType.WHEELCHAIR_PUSHES, "count"),
}
