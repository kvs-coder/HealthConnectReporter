# 1. Introduction and Goals

HealthConnectReporter lets Android apps and the `health_connect_reporter` Flutter plugin read, write and observe
Health Connect data through library-owned, JSON-serializable payloads.

| Priority | Quality goal | Scenario |
| :--- | :--- | :--- |
| 1 | Stable contract | A payload encoded by version 1.x decodes in the Flutter plugin without changes. |
| 2 | No client leakage | `library.api` mentions no `androidx.health.connect.client` type. |
| 3 | Parity | A concept shared with HealthKitReporter / SamsungHealthReporter has the same name and shape. |
| 4 | Testability | Every payload, type and service path runs in JVM unit tests (100 % line coverage at bootstrap). |
