# HealthConnectReporter

[![CI](https://github.com/kvs-coder/HealthConnectReporter/actions/workflows/ci.yml/badge.svg)](https://github.com/kvs-coder/HealthConnectReporter/actions/workflows/ci.yml)
[![JitPack](https://jitpack.io/v/kvs-coder/HealthConnectReporter.svg)](https://jitpack.io/#kvs-coder/HealthConnectReporter)

## About

A Kotlin wrapper above [Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect) for reading, writing and observing health data on Android.
The library turns Health Connect records into plain, `@Serializable` Kotlin data classes, so every result encodes to a simple JSON payload, and turns them back into records when you write.
Health Connect types never appear in the public API: you work with `HealthType`, `HeartRate`, `TimeRange` and friends.

It is the Health Connect sibling of [HealthKitReporter](https://github.com/kvs-coder/HealthKitReporter) and [SamsungHealthReporter](https://github.com/kvs-coder/SamsungHealthReporter) and follows the same design: one `HealthConnectReporter` with a **reader**, a **writer**, an **observer** and a **manager**.

> **Status**: under construction. The facade, availability check, types and payloads exist; the services follow (see `AGENTS.md`). Nothing is released yet.

## Requirements

- Android 8 (API 26) or later. Health Connect is part of Android 14+; on Android 9–13 the user installs the Health Connect app from Google Play.
- `androidx.health.connect:connect-client` 1.1.0 (pulled in by this library).

## Installation

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

// app/build.gradle.kts
dependencies {
    implementation("com.github.kvs-coder:HealthConnectReporter:0.0.0") // x-release-please-version
}
```

## Usage

```kotlin
when (HealthConnectReporter.availability(context)) {
    Availability.AVAILABLE -> {
        val reporter = HealthConnectReporter(context)
    }
    Availability.UPDATE_REQUIRED -> { /* send the user to Health Connect on Google Play */ }
    Availability.UNAVAILABLE -> { /* Health Connect does not run on this device */ }
}
```

### Manifest

Your app declares every health permission it requests, for example:

```xml
<uses-permission android:name="android.permission.health.READ_STEPS" />
<uses-permission android:name="android.permission.health.WRITE_STEPS" />
```

It also needs an activity that shows your privacy policy for the `androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE` intent and, on Android 14+, an activity-alias for `android.intent.action.VIEW_PERMISSION_USAGE` with the `android.intent.category.HEALTH_PERMISSIONS` category. Google Play requires a [Health Connect permissions declaration](https://support.google.com/googleplay/android-developer/answer/12991134) before you publish.

## Types

Every Health Connect record type is a `HealthType` with a `@Serializable` payload. Values use one unit per field: kg, kcal, m, m/s, W, mmHg, °C, L, mmol/L and percent; timestamps are epoch milliseconds, zone offsets seconds.

| HealthType | Identifier | Payload | Needs feature |
| :--- | :--- | :--- | :--- |
| `ACTIVE_CALORIES_BURNED` | `active_calories_burned` | `ActiveCaloriesBurned` |  |
| `BASAL_BODY_TEMPERATURE` | `basal_body_temperature` | `BasalBodyTemperature` |  |
| `BASAL_METABOLIC_RATE` | `basal_metabolic_rate` | `BasalMetabolicRate` |  |
| `BLOOD_GLUCOSE` | `blood_glucose` | `BloodGlucose` |  |
| `BLOOD_PRESSURE` | `blood_pressure` | `BloodPressure` |  |
| `BODY_FAT` | `body_fat` | `BodyFat` |  |
| `BODY_TEMPERATURE` | `body_temperature` | `BodyTemperature` |  |
| `BODY_WATER_MASS` | `body_water_mass` | `BodyWaterMass` |  |
| `BONE_MASS` | `bone_mass` | `BoneMass` |  |
| `CERVICAL_MUCUS` | `cervical_mucus` | `CervicalMucus` |  |
| `CYCLING_PEDALING_CADENCE` | `cycling_pedaling_cadence` | `CyclingPedalingCadence` |  |
| `DISTANCE` | `distance` | `Distance` |  |
| `ELEVATION_GAINED` | `elevation_gained` | `ElevationGained` |  |
| `EXERCISE_SESSION` | `exercise` | `ExerciseSession` |  |
| `FLOORS_CLIMBED` | `floors_climbed` | `FloorsClimbed` |  |
| `HEART_RATE` | `heart_rate` | `HeartRate` |  |
| `HEART_RATE_VARIABILITY_RMSSD` | `heart_rate_variability_rmssd` | `HeartRateVariabilityRmssd` |  |
| `HEIGHT` | `height` | `Height` |  |
| `HYDRATION` | `water_intake` | `Hydration` |  |
| `INTERMENSTRUAL_BLEEDING` | `intermenstrual_bleeding` | `IntermenstrualBleeding` |  |
| `LEAN_BODY_MASS` | `lean_body_mass` | `LeanBodyMass` |  |
| `MENSTRUATION_FLOW` | `menstruation_flow` | `MenstruationFlow` |  |
| `MENSTRUATION_PERIOD` | `menstruation_period` | `MenstruationPeriod` |  |
| `MINDFULNESS_SESSION` | `mindfulness_session` | `MindfulnessSession` | `MINDFULNESS_SESSION` |
| `NUTRITION` | `nutrition` | `Nutrition` |  |
| `OVULATION_TEST` | `ovulation_test` | `OvulationTest` |  |
| `OXYGEN_SATURATION` | `blood_oxygen` | `OxygenSaturation` |  |
| `PLANNED_EXERCISE_SESSION` | `planned_exercise` | `PlannedExerciseSession` | `PLANNED_EXERCISE` |
| `POWER` | `power` | `Power` |  |
| `RESPIRATORY_RATE` | `respiratory_rate` | `RespiratoryRate` |  |
| `RESTING_HEART_RATE` | `resting_heart_rate` | `RestingHeartRate` |  |
| `SEXUAL_ACTIVITY` | `sexual_activity` | `SexualActivity` |  |
| `SKIN_TEMPERATURE` | `skin_temperature` | `SkinTemperature` | `SKIN_TEMPERATURE` |
| `SLEEP_SESSION` | `sleep` | `SleepSession` |  |
| `SPEED` | `speed` | `Speed` |  |
| `STEPS` | `steps` | `Steps` |  |
| `STEPS_CADENCE` | `steps_cadence` | `StepsCadence` |  |
| `TOTAL_CALORIES_BURNED` | `total_calories_burned` | `TotalCaloriesBurned` |  |
| `VO2_MAX` | `vo2_max` | `Vo2Max` |  |
| `WEIGHT` | `weight` | `Weight` |  |
| `WHEELCHAIR_PUSHES` | `wheelchair_pushes` | `WheelchairPushes` |  |

```kotlin
val steps = Steps(
    startTimestamp = start,
    endTimestamp = end,
    harmonized = Steps.Harmonized(count = 1200),
)
val json = steps.json                             // {"type":"steps","startTimestamp":…}
val sample = Sample.serializer()                  // decodes any payload by its "type"
val fromFlutter = Steps.make(from = map)          // throws HealthConnectException.InvalidValue on bad input
val type = HealthType.make(from = "heart_rate")
```

## License

[MIT](LICENSE)
