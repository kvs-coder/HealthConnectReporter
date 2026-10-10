package com.kvs.healthconnectreporter.example.service

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
import com.kvs.healthconnectreporter.model.payload.WritableSample
import com.kvs.healthconnectreporter.model.type.BloodPressureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.BodyPosition
import com.kvs.healthconnectreporter.model.type.BodyTemperatureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.CervicalMucusAppearance
import com.kvs.healthconnectreporter.model.type.CervicalMucusSensation
import com.kvs.healthconnectreporter.model.type.ExercisePhase
import com.kvs.healthconnectreporter.model.type.ExerciseSegmentType
import com.kvs.healthconnectreporter.model.type.ExerciseType
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.MealType
import com.kvs.healthconnectreporter.model.type.MenstruationFlowType
import com.kvs.healthconnectreporter.model.type.MindfulnessSessionType
import com.kvs.healthconnectreporter.model.type.OvulationTestResult
import com.kvs.healthconnectreporter.model.type.ProtectionUsed
import com.kvs.healthconnectreporter.model.type.RelationToMeal
import com.kvs.healthconnectreporter.model.type.SkinTemperatureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.SleepStageType
import com.kvs.healthconnectreporter.model.type.SpecimenSource
import com.kvs.healthconnectreporter.model.type.Vo2MaxMeasurementMethod

/**
 * One valid sample of this type over the half hour before [end], for the writer demos.
 *
 * @param end epoch milliseconds
 */
@Suppress("LongMethod") // one sample per HealthType, kept in one exhaustive when
fun HealthType.demoSample(end: Long): WritableSample {
    val start = end - HALF_HOUR
    val mid = end - HALF_HOUR / 2
    return when (this) {
        HealthType.ACTIVE_CALORIES_BURNED ->
            ActiveCaloriesBurned(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    ActiveCaloriesBurned.Harmonized(energy = 120.5),
            )
        HealthType.BASAL_BODY_TEMPERATURE ->
            BasalBodyTemperature(
                startTimestamp = start,
                harmonized =
                    BasalBodyTemperature.Harmonized(36.6, BodyTemperatureMeasurementLocation.MOUTH),
            )
        HealthType.BASAL_METABOLIC_RATE ->
            BasalMetabolicRate(
                startTimestamp = start,
                harmonized =
                    BasalMetabolicRate.Harmonized(basalMetabolicRate = 70.0),
            )
        HealthType.BLOOD_GLUCOSE ->
            BloodGlucose(
                startTimestamp = start,
                harmonized =
                    BloodGlucose.Harmonized(
                        5.5,
                        SpecimenSource.CAPILLARY_BLOOD,
                        MealType.BREAKFAST,
                        RelationToMeal.AFTER_MEAL,
                    ),
            )
        HealthType.BLOOD_PRESSURE ->
            BloodPressure(
                startTimestamp = start,
                harmonized =
                    BloodPressure.Harmonized(
                        120.0,
                        80.0,
                        BodyPosition.SITTING_DOWN,
                        BloodPressureMeasurementLocation.LEFT_UPPER_ARM,
                    ),
            )
        HealthType.BODY_FAT ->
            BodyFat(
                startTimestamp = start,
                harmonized =
                    BodyFat.Harmonized(percentage = 18.5),
            )
        HealthType.BODY_TEMPERATURE ->
            BodyTemperature(
                startTimestamp = start,
                harmonized =
                    BodyTemperature.Harmonized(36.8, BodyTemperatureMeasurementLocation.ARMPIT),
            )
        HealthType.BODY_WATER_MASS ->
            BodyWaterMass(
                startTimestamp = start,
                harmonized =
                    BodyWaterMass.Harmonized(mass = 40.0),
            )
        HealthType.BONE_MASS ->
            BoneMass(
                startTimestamp = start,
                harmonized =
                    BoneMass.Harmonized(mass = 3.2),
            )
        HealthType.CERVICAL_MUCUS ->
            CervicalMucus(
                startTimestamp = start,
                harmonized =
                    CervicalMucus.Harmonized(CervicalMucusAppearance.CREAMY, CervicalMucusSensation.MEDIUM),
            )
        HealthType.CYCLING_PEDALING_CADENCE ->
            CyclingPedalingCadence(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    CyclingPedalingCadence.Harmonized(listOf(CyclingPedalingCadence.Series(mid, 80.0))),
            )
        HealthType.DISTANCE ->
            Distance(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    Distance.Harmonized(distance = 1500.0),
            )
        HealthType.ELEVATION_GAINED ->
            ElevationGained(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    ElevationGained.Harmonized(elevation = 25.0),
            )
        HealthType.EXERCISE_SESSION ->
            ExerciseSession(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    ExerciseSession.Harmonized(
                        exerciseType = ExerciseType.RUNNING,
                        title = "Morning run",
                        notes = "Easy",
                        segments =
                            listOf(
                                ExerciseSession.Segment(start, mid, ExerciseSegmentType.RUNNING, repetitions = 0),
                            ),
                        laps = listOf(ExerciseSession.Lap(start, mid, length = 400.0)),
                        route =
                            ExerciseSession.Route(
                                listOf(ExerciseSession.Location(mid, 52.52, 13.405, 5.0, 3.0, 34.0)),
                            ),
                    ),
            )
        HealthType.FLOORS_CLIMBED ->
            FloorsClimbed(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    FloorsClimbed.Harmonized(floors = 3.0),
            )
        HealthType.HEART_RATE ->
            HeartRate(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    HeartRate.Harmonized(listOf(HeartRate.Series(start, 70), HeartRate.Series(mid, 90))),
            )
        HealthType.HEART_RATE_VARIABILITY_RMSSD ->
            HeartRateVariabilityRmssd(
                startTimestamp = start,
                harmonized =
                    HeartRateVariabilityRmssd.Harmonized(heartRateVariabilityMillis = 45.0),
            )
        HealthType.HEIGHT ->
            Height(
                startTimestamp = start,
                harmonized =
                    Height.Harmonized(height = 1.8),
            )
        HealthType.HYDRATION ->
            Hydration(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    Hydration.Harmonized(volume = 0.5),
            )
        HealthType.INTERMENSTRUAL_BLEEDING ->
            IntermenstrualBleeding(
                startTimestamp = start,
            )
        HealthType.LEAN_BODY_MASS ->
            LeanBodyMass(
                startTimestamp = start,
                harmonized =
                    LeanBodyMass.Harmonized(mass = 60.0),
            )
        HealthType.MENSTRUATION_FLOW ->
            MenstruationFlow(
                startTimestamp = start,
                harmonized =
                    MenstruationFlow.Harmonized(MenstruationFlowType.MEDIUM),
            )
        HealthType.MENSTRUATION_PERIOD ->
            MenstruationPeriod(
                startTimestamp = start,
                endTimestamp = end,
            )
        HealthType.MINDFULNESS_SESSION ->
            MindfulnessSession(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    MindfulnessSession.Harmonized(MindfulnessSessionType.MEDITATION, "Calm", "Breathe"),
            )
        HealthType.NUTRITION ->
            Nutrition(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    Nutrition.Harmonized(
                        biotin = 0.00001,
                        caffeine = 0.00002,
                        calcium = 0.00003,
                        energy = 500.0,
                        energyFromFat = 90.0,
                        chloride = 0.00006,
                        cholesterol = 0.00007,
                        chromium = 0.00008,
                        copper = 0.00009,
                        dietaryFiber = 0.00010,
                        folate = 0.00011,
                        folicAcid = 0.00012,
                        iodine = 0.00013,
                        iron = 0.00014,
                        magnesium = 0.00015,
                        manganese = 0.00016,
                        molybdenum = 0.00017,
                        monounsaturatedFat = 0.00018,
                        niacin = 0.00019,
                        pantothenicAcid = 0.00020,
                        phosphorus = 0.00021,
                        polyunsaturatedFat = 0.00022,
                        potassium = 0.00023,
                        protein = 0.00024,
                        riboflavin = 0.00025,
                        saturatedFat = 0.00026,
                        selenium = 0.00027,
                        sodium = 0.00028,
                        sugar = 0.00029,
                        thiamin = 0.00030,
                        totalCarbohydrate = 0.00031,
                        totalFat = 0.00032,
                        transFat = 0.00033,
                        unsaturatedFat = 0.00034,
                        vitaminA = 0.00035,
                        vitaminB12 = 0.00036,
                        vitaminB6 = 0.00037,
                        vitaminC = 0.00038,
                        vitaminD = 0.00039,
                        vitaminE = 0.00040,
                        vitaminK = 0.00041,
                        zinc = 0.00042,
                        name = "Lunch",
                        mealType = MealType.LUNCH,
                    ),
            )
        HealthType.OVULATION_TEST ->
            OvulationTest(
                startTimestamp = start,
                harmonized =
                    OvulationTest.Harmonized(OvulationTestResult.POSITIVE),
            )
        HealthType.OXYGEN_SATURATION ->
            OxygenSaturation(
                startTimestamp = start,
                harmonized =
                    OxygenSaturation.Harmonized(percentage = 98.0),
            )
        HealthType.PLANNED_EXERCISE_SESSION ->
            PlannedExerciseSession(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    PlannedExerciseSession.Harmonized(
                        exerciseType = ExerciseType.RUNNING,
                        blocks =
                            listOf(
                                PlannedExerciseSession.Block(
                                    repetitions = 2,
                                    steps =
                                        listOf(
                                            PlannedExerciseSession.Step(
                                                exerciseType = ExerciseSegmentType.RUNNING,
                                                exercisePhase = ExercisePhase.WARMUP,
                                                completionGoal =
                                                    PlannedExerciseSession.CompletionGoal.Duration(
                                                        600_000,
                                                    ),
                                                performanceTargets =
                                                    listOf(
                                                        PlannedExerciseSession.PerformanceTarget.HeartRate(
                                                            100.0,
                                                            120.0,
                                                        ),
                                                    ),
                                                description = "Warm up",
                                            ),
                                            PlannedExerciseSession.Step(
                                                exerciseType = ExerciseSegmentType.RUNNING,
                                                exercisePhase = ExercisePhase.ACTIVE,
                                                completionGoal = PlannedExerciseSession.CompletionGoal.Distance(1000.0),
                                            ),
                                        ),
                                    description = "Intervals",
                                ),
                            ),
                        title = "Intervals",
                        notes = "Twice",
                    ),
            )
        HealthType.POWER ->
            Power(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    Power.Harmonized(listOf(Power.Series(mid, 250.0))),
            )
        HealthType.RESPIRATORY_RATE ->
            RespiratoryRate(
                startTimestamp = start,
                harmonized =
                    RespiratoryRate.Harmonized(rate = 16.0),
            )
        HealthType.RESTING_HEART_RATE ->
            RestingHeartRate(
                startTimestamp = start,
                harmonized =
                    RestingHeartRate.Harmonized(beatsPerMinute = 55),
            )
        HealthType.SEXUAL_ACTIVITY ->
            SexualActivity(
                startTimestamp = start,
                harmonized =
                    SexualActivity.Harmonized(ProtectionUsed.PROTECTED),
            )
        HealthType.SKIN_TEMPERATURE ->
            SkinTemperature(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    SkinTemperature.Harmonized(
                        listOf(SkinTemperature.Delta(mid, 0.5)),
                        33.5,
                        SkinTemperatureMeasurementLocation.WRIST,
                    ),
            )
        HealthType.SLEEP_SESSION ->
            SleepSession(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    SleepSession.Harmonized(
                        title = "Night",
                        notes = "Restful",
                        stages =
                            listOf(
                                SleepSession.Stage(start, mid, SleepStageType.LIGHT),
                                SleepSession.Stage(mid, end, SleepStageType.DEEP),
                            ),
                    ),
            )
        HealthType.SPEED ->
            Speed(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    Speed.Harmonized(listOf(Speed.Series(mid, 3.5))),
            )
        HealthType.STEPS ->
            Steps(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    Steps.Harmonized(count = 1200),
            )
        HealthType.STEPS_CADENCE ->
            StepsCadence(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    StepsCadence.Harmonized(listOf(StepsCadence.Series(mid, 160.0))),
            )
        HealthType.TOTAL_CALORIES_BURNED ->
            TotalCaloriesBurned(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    TotalCaloriesBurned.Harmonized(energy = 300.0),
            )
        HealthType.VO2_MAX ->
            Vo2Max(
                startTimestamp = start,
                harmonized =
                    Vo2Max.Harmonized(45.0, Vo2MaxMeasurementMethod.COOPER_TEST),
            )
        HealthType.WEIGHT ->
            Weight(
                startTimestamp = start,
                harmonized =
                    Weight.Harmonized(weight = 72.5),
            )
        HealthType.WHEELCHAIR_PUSHES ->
            WheelchairPushes(
                startTimestamp = start,
                endTimestamp = end,
                harmonized =
                    WheelchairPushes.Harmonized(count = 50),
            )
    }
}

private const val HALF_HOUR = 1_800_000L
