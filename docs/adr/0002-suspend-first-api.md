# 2. Suspend-first API

Date: 10.10.2026

## Status

Accepted

## Context

Every `HealthConnectClient` call is a `suspend fun`. HealthKitReporter uses completion handlers because HealthKit does; SamsungHealthReporter already exposes suspend functions.

## Decision

Every service call is a `suspend fun` running on the caller's coroutine context; observing is a cold `Flow`. The library never blocks a thread and never picks a dispatcher.

## Consequences

- Java callers need a coroutine bridge; the Flutter plugin calls the library from a plugin-owned scope.
