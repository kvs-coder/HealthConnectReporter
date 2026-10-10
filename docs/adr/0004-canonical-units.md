# 4. One canonical unit per payload field

Date: 10.10.2026

## Status

Accepted

## Context

Health Connect values carry unit objects (`Mass`, `Energy`, `Length`, `Pressure`, …) that convert on read. JSON consumers (the Flutter plugin) need plain numbers, and SamsungHealthReporter fixes one unit per field.

## Decision

Every payload field stores one canonical unit — kg, kcal, m, m/s, W, mmHg, °C, L, mmol/L, percent 0–100 — documented in its KDoc. `decorator/Extensions+Units.kt` is the only place that converts. Durations are epoch/duration milliseconds, zone offsets seconds.

## Consequences

- Consumers convert for display themselves; the payload never carries a unit string except `Aggregate.unit`.
