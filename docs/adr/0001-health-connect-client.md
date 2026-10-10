# 1. Build on the Health Connect client

Date: 10.10.2026

## Status

Accepted

## Context

Google deprecated the Google Fit Android and REST APIs and shuts them down in 2026; Health Connect replaces them. It is part of Android 14+ and ships as a Google Play app on Android 9–13. Apps reach it through the Jetpack library `androidx.health.connect:connect-client` (1.1.0 stable), a public artifact on Google Maven with suspend APIs. Samsung Health, Fitbit and most fitness apps sync into it.

## Decision

Build HealthConnectReporter on `connect-client` 1.1.0 with the architecture of HealthKitReporter and SamsungHealthReporter: one `HealthConnectReporter` facade sharing one `HealthConnectClient` between `HealthConnectReader`, `HealthConnectWriter`, `HealthConnectObserver` and `HealthConnectManager`; library-owned `HealthType`s and `@Serializable` payloads; no client type in the public API (checked by the binary-compatibility validator's `library.api`). The client is an `implementation` dependency, so consumers get it transitively without compiling against it.

## Consequences

- `minSdk` is 26, the client's own minimum; `availability` reports `UNAVAILABLE` where Health Connect can't run.
- Unlike the Samsung SDK, nothing is login-gated: CI builds against the real client, and there is no stub module.
- Consumers declare health permissions and the privacy-policy activity in their own manifest; the library declares none.
