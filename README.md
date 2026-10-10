# HealthConnectReporter

[![CI](https://github.com/kvs-coder/HealthConnectReporter/actions/workflows/ci.yml/badge.svg)](https://github.com/kvs-coder/HealthConnectReporter/actions/workflows/ci.yml)
[![JitPack](https://jitpack.io/v/kvs-coder/HealthConnectReporter.svg)](https://jitpack.io/#kvs-coder/HealthConnectReporter)

## About

A Kotlin wrapper above [Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect) for reading, writing and observing health data on Android.
The library turns Health Connect records into plain, `@Serializable` Kotlin data classes, so every result encodes to a simple JSON payload, and turns them back into records when you write.
Health Connect types never appear in the public API: you work with `HealthType`, `HeartRate`, `TimeRange` and friends.

It is the Health Connect sibling of [HealthKitReporter](https://github.com/kvs-coder/HealthKitReporter) and [SamsungHealthReporter](https://github.com/kvs-coder/SamsungHealthReporter) and follows the same design: one `HealthConnectReporter` with a **reader**, a **writer**, an **observer** and a **manager**.

> **Status**: under construction. The facade, availability check, types, payloads and all four services (reader, writer, observer, manager) and the demo app exist (see `AGENTS.md`). Nothing is released yet.

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
    implementation("com.github.kvs-coder:HealthConnectReporter:1.0.0") // x-release-please-version
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

### Reading

```kotlin
val reporter = HealthConnectReporter(context)
val range = TimeRange.lastDays(7)

// All heart rate records of the last week, newest first
val samples: List<Sample> = reporter.reader.read(HealthType.HEART_RATE, range)

// Page by page, oldest first, only from one app
val page = reporter.reader.readPage(
    type = HealthType.STEPS,
    range = range,
    pageSize = 500,
    ordering = Ordering.ASCENDING,
    origins = setOf(DataOrigin("com.google.android.apps.fitness")),
)
val next = page.nextPageToken?.let { reporter.reader.readPage(HealthType.STEPS, range, pageToken = it) }

// One record by id
val sample = reporter.reader.read(HealthType.STEPS, id = "…")

// Totals, and totals per day in the user's zone
val steps: Aggregate = reporter.reader.aggregate(Aggregation.STEPS_COUNT_TOTAL, range)
val daily: List<Aggregate> = reporter.reader.aggregate(
    aggregations = setOf(Aggregation.STEPS_COUNT_TOTAL, Aggregation.DISTANCE_DISTANCE_TOTAL),
    range = range,
    group = TimeGroup.Period(TimeGroupUnit.DAILY),
)
val quarterHours = reporter.reader.aggregate(setOf(Aggregation.HEART_RATE_BPM_AVG), range, TimeGroup.Duration(900_000))
```

Without the `READ_HEALTH_DATA_HISTORY` permission Health Connect returns only data from 30 days before the first permission grant onwards. Types that need a Health Connect feature (`HealthType.feature`) throw `HealthConnectException.NotAvailable` when the device lacks it.

### Writing

```kotlin
// Insert: missing client record ids are generated; the result carries the Health Connect ids
val stored: WritableSample = reporter.writer.insert(
    Steps(startTimestamp = start, endTimestamp = end, harmonized = Steps.Harmonized(count = 1200)),
)
val batch: List<WritableSample> = reporter.writer.insert(listOf(weight, heartRate))

// Update: by clientRecordId (bump clientRecordVersion to win), else by id
reporter.writer.update((stored as Steps).copy(harmonized = Steps.Harmonized(count = 1300), clientRecordVersion = 2))

// Delete
reporter.writer.delete(stored)
reporter.writer.delete(HealthType.STEPS, ids = listOf("…"), clientRecordIds = listOf("…"))
reporter.writer.delete(HealthType.STEPS, TimeRange.day())
```

Samples are written as manual entries unless `recordingMethod` says otherwise; actively and automatically recorded samples need a `device`.

### Observing changes

```kotlin
// Once: remember where the change history ends
val anchor: Anchor = reporter.observer.anchor(setOf(HealthType.STEPS, HealthType.HEART_RATE))
prefs.edit { putString("anchor", anchor.json) }

// Later: everything that changed since, and the anchor to store next
val changes: ChangeSet = reporter.observer.changes(Anchor.decode(prefs.getString("anchor", null)!!))
changes.upserted   // inserted or updated samples
changes.deletedIds // ids of deleted records
prefs.edit { putString("anchor", changes.anchor.json) }

// While on screen: poll every minute, only non-empty change sets
reporter.observer.observe(anchor).collect { changeSet -> /* … */ }
```

Health Connect keeps changes for 30 days; an older anchor throws `HealthConnectException.AnchorExpired`, after which you read the data again in full and take a new anchor.

#### In the background

The observer does not schedule work. Use WorkManager and request the `android.permission.health.READ_HEALTH_DATA_IN_BACKGROUND` permission (check `Feature.READ_HEALTH_DATA_IN_BACKGROUND` first):

```kotlin
class HealthSyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val reporter = HealthConnectReporter(applicationContext)
        val stored = store.anchor() ?: return Result.success()
        return try {
            val changes = reporter.observer.changes(stored)
            upload(changes)
            store.save(changes.anchor)
            Result.success()
        } catch (expired: HealthConnectException.AnchorExpired) {
            store.save(reporter.observer.anchor(types))
            Result.success() // read the data again in full
        } catch (failure: HealthConnectException.Platform) {
            Result.retry()
        }
    }
}

WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "health-sync",
    ExistingPeriodicWorkPolicy.KEEP,
    PeriodicWorkRequestBuilder<HealthSyncWorker>(15, TimeUnit.MINUTES).build(),
)
```

### Permissions and Health Connect screens

```kotlin
// Ask for read and write access to every type you use (each must be in your manifest)
val granted: Set<Permission> = reporter.manager.requestPermissions(
    activity = this, // a ComponentActivity
    permissions = Permission.of(listOf(HealthType.STEPS, HealthType.HEART_RATE)) + Permission.ReadHistory,
)

// Or register the library-typed contract yourself
val request = registerForActivityResult(reporter.manager.permissionContract()) { granted -> /* … */ }
request.launch(setOf(Permission.Data(HealthType.WEIGHT, AccessType.READ)))

reporter.manager.grantedPermissions()
reporter.manager.revokeAllPermissions()
reporter.manager.isFeatureAvailable(Feature.SKIN_TEMPERATURE)
startActivity(reporter.manager.settingsIntent(context))
startActivity(reporter.manager.installIntent(context)) // when availability is UPDATE_REQUIRED
```

Requesting a permission the manifest does not declare throws `HealthConnectException.InvalidValue`; a permission that needs a missing feature throws `NotAvailable`. `STEPS` / `STEPS_CADENCE`, `EXERCISE_SESSION` / `CYCLING_PEDALING_CADENCE` and `MENSTRUATION_FLOW` / `MENSTRUATION_PERIOD` share one Health Connect permission each, so granting one grants both.

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

## Example app

`app/` is a Jetpack Compose demo with one row per public call: a Manager, Reader, Writer and Observer section, rows generated from `HealthType.entries` and `Aggregation.entries`. Its manifest declares every health permission, the permissions rationale activity and the `VIEW_PERMISSION_USAGE` activity-alias, so it doubles as a manifest template. Run it on an Android 14+ device or emulator, start with *Manager › Request every permission*, then insert data in *Writer* and read it back.

## License

[MIT](LICENSE)
