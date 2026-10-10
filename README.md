# HealthConnectReporter

[![CI](https://github.com/kvs-coder/HealthConnectReporter/actions/workflows/ci.yml/badge.svg)](https://github.com/kvs-coder/HealthConnectReporter/actions/workflows/ci.yml)
[![JitPack](https://jitpack.io/v/kvs-coder/HealthConnectReporter.svg)](https://jitpack.io/#kvs-coder/HealthConnectReporter)

## About

A Kotlin wrapper above [Health Connect](https://developer.android.com/health-and-fitness/guides/health-connect) for reading, writing and observing health data on Android.
The library turns Health Connect records into plain, `@Serializable` Kotlin data classes, so every result encodes to a simple JSON payload, and turns them back into records when you write.
Health Connect types never appear in the public API: you work with `HealthType`, `HeartRate`, `TimeRange` and friends.

It is the Health Connect sibling of [HealthKitReporter](https://github.com/kvs-coder/HealthKitReporter) and [SamsungHealthReporter](https://github.com/kvs-coder/SamsungHealthReporter) and follows the same design: one `HealthConnectReporter` with a **reader**, a **writer**, an **observer** and a **manager**.

> **Status**: under construction. The facade and availability check exist; the services follow (see `AGENTS.md`). Nothing is released yet.

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

## License

[MIT](LICENSE)
