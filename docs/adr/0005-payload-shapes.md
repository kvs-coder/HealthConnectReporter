# 5. Payload shapes for Health Connect records

Date: 10.10.2026

## Status

Accepted

## Context

Payloads follow SamsungHealthReporter's shape (`startTimestamp`, `endTimestamp`, `harmonized`, identity, `Payload.Factory`), but Health Connect records differ: some carry no values, instantaneous records have one time, write metadata comes only from `Metadata` factories, and route access can need the user's consent.

## Decision

- `HealthType.identifier` equals SamsungHealthReporter's where the concept matches (`HYDRATION` → `water_intake`, `OXYGEN_SATURATION` → `blood_oxygen`, `EXERCISE_SESSION` → `exercise`, `SLEEP_SESSION` → `sleep`); the payload's `@SerialName` is the identifier.
- Instantaneous records store their time in `startTimestamp`; `endTimestamp` and `endZoneOffsetSeconds` are null.
- Records without values (`IntermenstrualBleeding`, `MenstruationPeriod`) have no `harmonized`.
- Write metadata comes from the `Metadata` factory of the sample's recording method (see ADR 0006 for identity); actively and automatically recorded samples need a `device`.
- `ExerciseSession.harmonized.isRouteConsentRequired` reports a route the app may not read yet; it is ignored when written.
- Planned steps' goals and targets are sealed `CompletionGoal` / `PerformanceTarget` hierarchies with a `type` discriminator; unknown client goals map to `Unknown`.
- Value enums map Health Connect int constants entry for entry; constants added later map to an `UNKNOWN` (or `OTHER_WORKOUT`, `OTHER`, `INCONCLUSIVE`) fallback.

## Consequences

- Dart models can share identifiers and most fields with the Samsung Health plugin.
- Kover branch coverage stays just below 100 %: `SerializationTest` drives the kotlinx.serialization-generated code (encoding with and without defaults, decoding with each key missing), and the few branches left are generated constructor code.
