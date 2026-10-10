package com.kvs.healthconnectreporter.model.type

import com.kvs.healthconnectreporter.HealthConnectException
import kotlinx.serialization.Serializable

/**
 * **HealthType** one Health Connect record type.
 *
 * Every type can be read, written by the app that holds its write permission, and observed.
 *
 * @property identifier **String** stable library identifier, e.g. `heart_rate`; equal to SamsungHealthReporter's
 * where the concept is the same
 * @property isWritable **Boolean** samples can be inserted, updated and deleted with **HealthConnectWriter**
 * @property feature **Feature?** the Health Connect feature the type needs, null when it needs none
 */
@Serializable
public enum class HealthType(
    public val identifier: String,
    public val isWritable: Boolean,
    public val feature: Feature?,
) {
    /** calories burned by activity, **ActiveCaloriesBurned** */
    ACTIVE_CALORIES_BURNED("active_calories_burned", true, null),

    /** basal body temperature, **BasalBodyTemperature** */
    BASAL_BODY_TEMPERATURE("basal_body_temperature", true, null),

    /** basal metabolic rate, **BasalMetabolicRate** */
    BASAL_METABOLIC_RATE("basal_metabolic_rate", true, null),

    /** blood glucose, **BloodGlucose** */
    BLOOD_GLUCOSE("blood_glucose", true, null),

    /** blood pressure, **BloodPressure** */
    BLOOD_PRESSURE("blood_pressure", true, null),

    /** body fat, **BodyFat** */
    BODY_FAT("body_fat", true, null),

    /** body temperature, **BodyTemperature** */
    BODY_TEMPERATURE("body_temperature", true, null),

    /** body water mass, **BodyWaterMass** */
    BODY_WATER_MASS("body_water_mass", true, null),

    /** bone mass, **BoneMass** */
    BONE_MASS("bone_mass", true, null),

    /** cervical mucus, **CervicalMucus** */
    CERVICAL_MUCUS("cervical_mucus", true, null),

    /** cycling pedaling cadence, **CyclingPedalingCadence** */
    CYCLING_PEDALING_CADENCE("cycling_pedaling_cadence", true, null),

    /** distance, **Distance** */
    DISTANCE("distance", true, null),

    /** elevation gained, **ElevationGained** */
    ELEVATION_GAINED("elevation_gained", true, null),

    /** exercise sessions, **ExerciseSession** */
    EXERCISE_SESSION("exercise", true, null),

    /** floors climbed, **FloorsClimbed** */
    FLOORS_CLIMBED("floors_climbed", true, null),

    /** heart rate, **HeartRate** */
    HEART_RATE("heart_rate", true, null),

    /** heart rate variability (RMSSD), **HeartRateVariabilityRmssd** */
    HEART_RATE_VARIABILITY_RMSSD("heart_rate_variability_rmssd", true, null),

    /** height, **Height** */
    HEIGHT("height", true, null),

    /** water intake, **Hydration** */
    HYDRATION("water_intake", true, null),

    /** intermenstrual bleeding, **IntermenstrualBleeding** */
    INTERMENSTRUAL_BLEEDING("intermenstrual_bleeding", true, null),

    /** lean body mass, **LeanBodyMass** */
    LEAN_BODY_MASS("lean_body_mass", true, null),

    /** menstrual flow, **MenstruationFlow** */
    MENSTRUATION_FLOW("menstruation_flow", true, null),

    /** menstruation periods, **MenstruationPeriod** */
    MENSTRUATION_PERIOD("menstruation_period", true, null),

    /** mindfulness sessions, **MindfulnessSession** (needs [Feature.MINDFULNESS_SESSION]) */
    MINDFULNESS_SESSION("mindfulness_session", true, Feature.MINDFULNESS_SESSION),

    /** nutrition, **Nutrition** */
    NUTRITION("nutrition", true, null),

    /** ovulation tests, **OvulationTest** */
    OVULATION_TEST("ovulation_test", true, null),

    /** blood oxygen saturation, **OxygenSaturation** */
    OXYGEN_SATURATION("blood_oxygen", true, null),

    /** planned exercise sessions (training plans), **PlannedExerciseSession** (needs [Feature.PLANNED_EXERCISE]) */
    PLANNED_EXERCISE_SESSION("planned_exercise", true, Feature.PLANNED_EXERCISE),

    /** power, **Power** */
    POWER("power", true, null),

    /** respiratory rate, **RespiratoryRate** */
    RESPIRATORY_RATE("respiratory_rate", true, null),

    /** resting heart rate, **RestingHeartRate** */
    RESTING_HEART_RATE("resting_heart_rate", true, null),

    /** sexual activity, **SexualActivity** */
    SEXUAL_ACTIVITY("sexual_activity", true, null),

    /** skin temperature, **SkinTemperature** (needs [Feature.SKIN_TEMPERATURE]) */
    SKIN_TEMPERATURE("skin_temperature", true, Feature.SKIN_TEMPERATURE),

    /** sleep sessions, **SleepSession** */
    SLEEP_SESSION("sleep", true, null),

    /** speed, **Speed** */
    SPEED("speed", true, null),

    /** steps, **Steps** */
    STEPS("steps", true, null),

    /** steps cadence, **StepsCadence** */
    STEPS_CADENCE("steps_cadence", true, null),

    /** total calories burned, **TotalCaloriesBurned** */
    TOTAL_CALORIES_BURNED("total_calories_burned", true, null),

    /** maximal oxygen uptake (VO2 max), **Vo2Max** */
    VO2_MAX("vo2_max", true, null),

    /** weight, **Weight** */
    WEIGHT("weight", true, null),

    /** wheelchair pushes, **WheelchairPushes** */
    WHEELCHAIR_PUSHES("wheelchair_pushes", true, null),
    ;

    /** **List<Aggregation>** the aggregations available for this type */
    public val aggregations: List<Aggregation> get() = Aggregation.entries.filter { it.healthType == this }

    /** Factory of **HealthType** */
    public companion object {
        /**
         * Finds a type by its identifier.
         *
         * @param from **String** the identifier, e.g. `heart_rate`
         * @return **HealthType** the type
         * @throws HealthConnectException.InvalidType when no type has this identifier
         */
        public fun make(from: String): HealthType =
            entries.firstOrNull { it.identifier == from }
                ?: throw HealthConnectException.InvalidType("Invalid HealthType identifier: $from")
    }
}
