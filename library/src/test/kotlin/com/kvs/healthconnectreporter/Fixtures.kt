package com.kvs.healthconnectreporter

import androidx.health.connect.client.records.Record
import androidx.health.connect.client.response.ReadRecordsResponse
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Payload
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
import com.kvs.healthconnectreporter.model.type.BloodPressureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.BodyPosition
import com.kvs.healthconnectreporter.model.type.BodyTemperatureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.CervicalMucusAppearance
import com.kvs.healthconnectreporter.model.type.CervicalMucusSensation
import com.kvs.healthconnectreporter.model.type.DeviceType
import com.kvs.healthconnectreporter.model.type.ExercisePhase
import com.kvs.healthconnectreporter.model.type.ExerciseSegmentType
import com.kvs.healthconnectreporter.model.type.ExerciseType
import com.kvs.healthconnectreporter.model.type.HealthType
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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull

const val START = 1_626_884_800_000L
const val MID = START + 1_800_000L
const val END = START + 3_600_000L
const val OFFSET = 7_200

val device = Device(DeviceType.WATCH, "Google", "Pixel Watch")

val activeCaloriesBurned =
    ActiveCaloriesBurned(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            ActiveCaloriesBurned.Harmonized(energy = 120.5),
        clientRecordId = "client-activeCaloriesBurned",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val basalBodyTemperature =
    BasalBodyTemperature(
        startTimestamp = START,
        harmonized =
            BasalBodyTemperature.Harmonized(36.6, BodyTemperatureMeasurementLocation.MOUTH),
        clientRecordId = "client-basalBodyTemperature",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val basalMetabolicRate =
    BasalMetabolicRate(
        startTimestamp = START,
        harmonized =
            BasalMetabolicRate.Harmonized(basalMetabolicRate = 70.0),
        clientRecordId = "client-basalMetabolicRate",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val bloodGlucose =
    BloodGlucose(
        startTimestamp = START,
        harmonized =
            BloodGlucose.Harmonized(5.5, SpecimenSource.CAPILLARY_BLOOD, MealType.BREAKFAST, RelationToMeal.AFTER_MEAL),
        clientRecordId = "client-bloodGlucose",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val bloodPressure =
    BloodPressure(
        startTimestamp = START,
        harmonized =
            BloodPressure.Harmonized(
                120.0,
                80.0,
                BodyPosition.SITTING_DOWN,
                BloodPressureMeasurementLocation.LEFT_UPPER_ARM,
            ),
        clientRecordId = "client-bloodPressure",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val bodyFat =
    BodyFat(
        startTimestamp = START,
        harmonized =
            BodyFat.Harmonized(percentage = 18.5),
        clientRecordId = "client-bodyFat",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val bodyTemperature =
    BodyTemperature(
        startTimestamp = START,
        harmonized =
            BodyTemperature.Harmonized(36.8, BodyTemperatureMeasurementLocation.ARMPIT),
        clientRecordId = "client-bodyTemperature",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val bodyWaterMass =
    BodyWaterMass(
        startTimestamp = START,
        harmonized =
            BodyWaterMass.Harmonized(mass = 40.0),
        clientRecordId = "client-bodyWaterMass",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val boneMass =
    BoneMass(
        startTimestamp = START,
        harmonized =
            BoneMass.Harmonized(mass = 3.2),
        clientRecordId = "client-boneMass",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val cervicalMucus =
    CervicalMucus(
        startTimestamp = START,
        harmonized =
            CervicalMucus.Harmonized(CervicalMucusAppearance.CREAMY, CervicalMucusSensation.MEDIUM),
        clientRecordId = "client-cervicalMucus",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val cyclingPedalingCadence =
    CyclingPedalingCadence(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            CyclingPedalingCadence.Harmonized(listOf(CyclingPedalingCadence.Series(MID, 80.0))),
        clientRecordId = "client-cyclingPedalingCadence",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val distance =
    Distance(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            Distance.Harmonized(distance = 1500.0),
        clientRecordId = "client-distance",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val elevationGained =
    ElevationGained(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            ElevationGained.Harmonized(elevation = 25.0),
        clientRecordId = "client-elevationGained",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val exerciseSession =
    ExerciseSession(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            ExerciseSession.Harmonized(
                exerciseType = ExerciseType.RUNNING,
                title = "Morning run",
                notes = "Easy",
                segments = listOf(ExerciseSession.Segment(START, MID, ExerciseSegmentType.RUNNING, repetitions = 0)),
                laps = listOf(ExerciseSession.Lap(START, MID, length = 400.0)),
                route =
                    ExerciseSession.Route(
                        listOf(ExerciseSession.Location(MID, 52.52, 13.405, 5.0, 3.0, 34.0)),
                    ),
                plannedExerciseSessionId = "planned-1",
            ),
        clientRecordId = "client-exerciseSession",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val floorsClimbed =
    FloorsClimbed(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            FloorsClimbed.Harmonized(floors = 3.0),
        clientRecordId = "client-floorsClimbed",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val heartRate =
    HeartRate(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            HeartRate.Harmonized(listOf(HeartRate.Series(START, 70), HeartRate.Series(MID, 90))),
        clientRecordId = "client-heartRate",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val heartRateVariabilityRmssd =
    HeartRateVariabilityRmssd(
        startTimestamp = START,
        harmonized =
            HeartRateVariabilityRmssd.Harmonized(heartRateVariabilityMillis = 45.0),
        clientRecordId = "client-heartRateVariabilityRmssd",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val height =
    Height(
        startTimestamp = START,
        harmonized =
            Height.Harmonized(height = 1.8),
        clientRecordId = "client-height",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val hydration =
    Hydration(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            Hydration.Harmonized(volume = 0.5),
        clientRecordId = "client-hydration",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val intermenstrualBleeding =
    IntermenstrualBleeding(
        startTimestamp = START,
        clientRecordId = "client-intermenstrualBleeding",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val leanBodyMass =
    LeanBodyMass(
        startTimestamp = START,
        harmonized =
            LeanBodyMass.Harmonized(mass = 60.0),
        clientRecordId = "client-leanBodyMass",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val menstruationFlow =
    MenstruationFlow(
        startTimestamp = START,
        harmonized =
            MenstruationFlow.Harmonized(MenstruationFlowType.MEDIUM),
        clientRecordId = "client-menstruationFlow",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val menstruationPeriod =
    MenstruationPeriod(
        startTimestamp = START,
        endTimestamp = END,
        clientRecordId = "client-menstruationPeriod",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val mindfulnessSession =
    MindfulnessSession(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            MindfulnessSession.Harmonized(MindfulnessSessionType.MEDITATION, "Calm", "Breathe"),
        clientRecordId = "client-mindfulnessSession",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val nutrition =
    Nutrition(
        startTimestamp = START,
        endTimestamp = END,
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
        clientRecordId = "client-nutrition",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val ovulationTest =
    OvulationTest(
        startTimestamp = START,
        harmonized =
            OvulationTest.Harmonized(OvulationTestResult.POSITIVE),
        clientRecordId = "client-ovulationTest",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val oxygenSaturation =
    OxygenSaturation(
        startTimestamp = START,
        harmonized =
            OxygenSaturation.Harmonized(percentage = 98.0),
        clientRecordId = "client-oxygenSaturation",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val plannedExerciseSession =
    PlannedExerciseSession(
        startTimestamp = START,
        endTimestamp = END,
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
                                        completionGoal = PlannedExerciseSession.CompletionGoal.Duration(600_000),
                                        performanceTargets =
                                            listOf(
                                                PlannedExerciseSession.PerformanceTarget.HeartRate(100.0, 120.0),
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
        clientRecordId = "client-plannedExerciseSession",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val power =
    Power(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            Power.Harmonized(listOf(Power.Series(MID, 250.0))),
        clientRecordId = "client-power",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val respiratoryRate =
    RespiratoryRate(
        startTimestamp = START,
        harmonized =
            RespiratoryRate.Harmonized(rate = 16.0),
        clientRecordId = "client-respiratoryRate",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val restingHeartRate =
    RestingHeartRate(
        startTimestamp = START,
        harmonized =
            RestingHeartRate.Harmonized(beatsPerMinute = 55),
        clientRecordId = "client-restingHeartRate",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val sexualActivity =
    SexualActivity(
        startTimestamp = START,
        harmonized =
            SexualActivity.Harmonized(ProtectionUsed.PROTECTED),
        clientRecordId = "client-sexualActivity",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val skinTemperature =
    SkinTemperature(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            SkinTemperature.Harmonized(
                listOf(SkinTemperature.Delta(MID, 0.5)),
                33.5,
                SkinTemperatureMeasurementLocation.WRIST,
            ),
        clientRecordId = "client-skinTemperature",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val sleepSession =
    SleepSession(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            SleepSession.Harmonized(
                title = "Night",
                notes = "Restful",
                stages =
                    listOf(
                        SleepSession.Stage(START, MID, SleepStageType.LIGHT),
                        SleepSession.Stage(MID, END, SleepStageType.DEEP),
                    ),
            ),
        clientRecordId = "client-sleepSession",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val speed =
    Speed(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            Speed.Harmonized(listOf(Speed.Series(MID, 3.5))),
        clientRecordId = "client-speed",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val steps =
    Steps(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            Steps.Harmonized(count = 1200),
        clientRecordId = "client-steps",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val stepsCadence =
    StepsCadence(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            StepsCadence.Harmonized(listOf(StepsCadence.Series(MID, 160.0))),
        clientRecordId = "client-stepsCadence",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

val totalCaloriesBurned =
    TotalCaloriesBurned(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            TotalCaloriesBurned.Harmonized(energy = 300.0),
        clientRecordId = "client-totalCaloriesBurned",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.AUTOMATICALLY_RECORDED,
        device = device,
    )

val vo2Max =
    Vo2Max(
        startTimestamp = START,
        harmonized =
            Vo2Max.Harmonized(45.0, Vo2MaxMeasurementMethod.COOPER_TEST),
        clientRecordId = "client-vo2Max",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.ACTIVELY_RECORDED,
        device = device,
    )

val weight =
    Weight(
        startTimestamp = START,
        harmonized =
            Weight.Harmonized(weight = 72.5),
        clientRecordId = "client-weight",
        clientRecordVersion = 1,
        recordingMethod = RecordingMethod.UNKNOWN,
        device = device,
    )

val wheelchairPushes =
    WheelchairPushes(
        startTimestamp = START,
        endTimestamp = END,
        harmonized =
            WheelchairPushes.Harmonized(count = 50),
        clientRecordId = "client-wheelchairPushes",
        clientRecordVersion = 1,
        startZoneOffsetSeconds = OFFSET,
        endZoneOffsetSeconds = OFFSET,
        recordingMethod = RecordingMethod.MANUAL_ENTRY,
    )

/** One fixture per **HealthType**. */
val allSamples: List<Sample> =
    listOf(
        activeCaloriesBurned,
        basalBodyTemperature,
        basalMetabolicRate,
        bloodGlucose,
        bloodPressure,
        bodyFat,
        bodyTemperature,
        bodyWaterMass,
        boneMass,
        cervicalMucus,
        cyclingPedalingCadence,
        distance,
        elevationGained,
        exerciseSession,
        floorsClimbed,
        heartRate,
        heartRateVariabilityRmssd,
        height,
        hydration,
        intermenstrualBleeding,
        leanBodyMass,
        menstruationFlow,
        menstruationPeriod,
        mindfulnessSession,
        nutrition,
        ovulationTest,
        oxygenSaturation,
        plannedExerciseSession,
        power,
        respiratoryRate,
        restingHeartRate,
        sexualActivity,
        skinTemperature,
        sleepSession,
        speed,
        steps,
        stepsCadence,
        totalCaloriesBurned,
        vo2Max,
        weight,
        wheelchairPushes,
    )

/** The factory of the payload of this type. */
val HealthType.factory: Payload.Factory<out Sample>
    get() =
        when (this) {
            HealthType.ACTIVE_CALORIES_BURNED -> ActiveCaloriesBurned
            HealthType.BASAL_BODY_TEMPERATURE -> BasalBodyTemperature
            HealthType.BASAL_METABOLIC_RATE -> BasalMetabolicRate
            HealthType.BLOOD_GLUCOSE -> BloodGlucose
            HealthType.BLOOD_PRESSURE -> BloodPressure
            HealthType.BODY_FAT -> BodyFat
            HealthType.BODY_TEMPERATURE -> BodyTemperature
            HealthType.BODY_WATER_MASS -> BodyWaterMass
            HealthType.BONE_MASS -> BoneMass
            HealthType.CERVICAL_MUCUS -> CervicalMucus
            HealthType.CYCLING_PEDALING_CADENCE -> CyclingPedalingCadence
            HealthType.DISTANCE -> Distance
            HealthType.ELEVATION_GAINED -> ElevationGained
            HealthType.EXERCISE_SESSION -> ExerciseSession
            HealthType.FLOORS_CLIMBED -> FloorsClimbed
            HealthType.HEART_RATE -> HeartRate
            HealthType.HEART_RATE_VARIABILITY_RMSSD -> HeartRateVariabilityRmssd
            HealthType.HEIGHT -> Height
            HealthType.HYDRATION -> Hydration
            HealthType.INTERMENSTRUAL_BLEEDING -> IntermenstrualBleeding
            HealthType.LEAN_BODY_MASS -> LeanBodyMass
            HealthType.MENSTRUATION_FLOW -> MenstruationFlow
            HealthType.MENSTRUATION_PERIOD -> MenstruationPeriod
            HealthType.MINDFULNESS_SESSION -> MindfulnessSession
            HealthType.NUTRITION -> Nutrition
            HealthType.OVULATION_TEST -> OvulationTest
            HealthType.OXYGEN_SATURATION -> OxygenSaturation
            HealthType.PLANNED_EXERCISE_SESSION -> PlannedExerciseSession
            HealthType.POWER -> Power
            HealthType.RESPIRATORY_RATE -> RespiratoryRate
            HealthType.RESTING_HEART_RATE -> RestingHeartRate
            HealthType.SEXUAL_ACTIVITY -> SexualActivity
            HealthType.SKIN_TEMPERATURE -> SkinTemperature
            HealthType.SLEEP_SESSION -> SleepSession
            HealthType.SPEED -> Speed
            HealthType.STEPS -> Steps
            HealthType.STEPS_CADENCE -> StepsCadence
            HealthType.TOTAL_CALORIES_BURNED -> TotalCaloriesBurned
            HealthType.VO2_MAX -> Vo2Max
            HealthType.WEIGHT -> Weight
            HealthType.WHEELCHAIR_PUSHES -> WheelchairPushes
        }

/** A Health Connect read response with one page of [records]. */
fun <T : Record> response(
    records: List<T>,
    pageToken: String? = null,
): ReadRecordsResponse<T> = ReadRecordsResponse(records, pageToken)

/** Parses JSON into the plain map a Flutter plugin would send. */
@Suppress("UNCHECKED_CAST") // a JSON object always parses to a string-keyed map
fun String.asMap(): Map<String, Any?> = Json.parseToJsonElement(this).plain as Map<String, Any?>

private val JsonElement.plain: Any?
    get() =
        when (this) {
            is JsonNull -> null
            is JsonObject -> mapValues { it.value.plain }
            is JsonArray -> map { it.plain }
            is JsonPrimitive ->
                when {
                    isString -> content
                    booleanOrNull != null -> booleanOrNull
                    longOrNull != null -> longOrNull
                    else -> doubleOrNull
                }
        }
