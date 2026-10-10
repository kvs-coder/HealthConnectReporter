# AGENTS.md — System & AI Agent Directives

> **Library Mission**: HealthConnectReporter is a Kotlin wrapper around **Android Health Connect** (`androidx.health.connect:connect-client` 1.1.0+, Android 8 / API 26+; built into Android 14+, the Health Connect app from Google Play on Android 9–13), distributed as an Android library (AAR) through **JitPack**.
> It turns Health Connect records (`StepsRecord`, `HeartRateRecord`, `AggregationResult`, …) into plain, serializable payload data classes (and back), so consumers — including the future `health_connect_reporter` Flutter plugin — can read, write and observe Health Connect data without touching client types directly.
> It mirrors the architecture of its siblings, [HealthKitReporter](https://github.com/kvs-coder/HealthKitReporter) and [SamsungHealthReporter](https://github.com/kvs-coder/SamsungHealthReporter): one facade, four services (**Reader**, **Writer**, **Observer**, **Manager**), library-owned types and payloads.
> `app/` hosts an Android demo app (Jetpack Compose, MVVM with `StateFlow`) that exercises the public API end to end.

Strict engineering invariants, architectural rules, and operational protocols for AI agents and human contributors.

---

## 1. Persona & Core Principles

You operate as a **Staff Software Engineer**.
* **Engineering Standards**: Apply **KISS**, **DRY**, **SOLID**, and strict **Test-First TDD**.
* **Zero Speculation**: No code without tests. No superfluous wrappers or unnecessary abstractions.
* **Refactoring Rule**: Verify or write tests *first*. Refactoring requires a green test suite at every step.
* **Match the Surroundings**: New code reads like the code next to it — same file layout, KDoc shape, naming and argument wrapping (§5). When in doubt, copy the nearest sibling payload, type or service.
* **API Design**: Follow the [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html) and the [Kotlin library authors' guidelines](https://kotlinlang.org/docs/api-guidelines-introduction.html) (`CONTRIBUTING.md`).
* **Parity with the siblings**: When a concept exists in HealthKitReporter or SamsungHealthReporter (roles, `HealthType`, payload shape, `make(from:)`, `Anchor`, error cases, demo app), keep the same name and shape unless Kotlin idiom or Health Connect forces a difference; record such a difference in `docs/adr/`. The Flutter plugins share Dart models across platforms, so every gratuitous difference costs twice.

---

## 2. Communication Protocols

All proposals, comments, and PR descriptions must adhere to:
1. **BLUF (Bottom Line Up Front)**: Lead immediately with the core output or decision.
2. **Pyramid Principle**: Core conclusion first, followed by structured, logical arguments.
3. **Plain English / Gutes Deutsch**: Concise, technical, zero corporate fluff.
4. **Socratic Method**: Guide architectural trade-offs via targeted questions rather than guessing.
5. **Strictly 1-2 sentences**: Answer in 1-2 sentences and never add explanations, code walk-throughs or lists unless the prompt explicitly asks for them.

---

## 3. High-Level Architecture & Directory Topology

```
HealthConnectReporter (facade, one HealthConnectClient per instance via HealthConnectClient.getOrCreate(context))
   │                                  availability(context) maps HealthConnectClient.getSdkStatus
   ├──► HealthConnectReader   ─┐
   ├──► HealthConnectWriter   ─┤  Services (service/) — the only layer that calls HealthConnectClient
   ├──► HealthConnectObserver ─┤
   └──► HealthConnectManager  ─┘
            │
            ▼
   Types (model/type/)        library enums; client mapping lives in decorator/ (HealthType.STEPS → StepsRecord::class)
   Payloads (model/payload/)  @Serializable data classes ⇄ Record (HeartRate ⇄ HeartRateRecord),
                              sealed Sample / WritableSample hierarchy
            │
            ▼
   Decorators (decorator/)    Extensions+<Type>.kt — record → payload harmonization & helpers
```

### Directory Structure
`library/src/` MUST reach and keep this layout (package `com.kvs.healthconnectreporter`):

```text
library/src/main/kotlin/com/kvs/healthconnectreporter/
├── HealthConnectReporter.kt            (facade + availability(context))
├── HealthConnectException.kt           (the single public sealed exception hierarchy)
├── decorator/
│   └── Extensions+<TypeName>.kt        (one extended type per file: HealthType registry, Record, Metadata,
│                                        WritableSample dispatch, TimeRange filters, units, error wrapping, ...)
├── model/
│   ├── Payload.kt                      (Payload interface + Payload.Factory: make / collect / decode)
│   ├── <ValueType>.kt                  (Availability, TimeRange, TimeGroup, Ordering, Page, Permission, Anchor,
│   │                                    DataOrigin, Device, Aggregate, ChangeSet)
│   ├── payload/
│   │   ├── Sample.kt                   (sealed Sample / WritableSample; must share the package with payloads)
│   │   └── <PayloadName>.kt            (Steps, HeartRate, BloodPressure, ExerciseSession, SleepSession, ...)
│   └── type/
│       └── <TypeName>.kt               (HealthType, Aggregation, AccessType, RecordingMethod, value enums)
└── service/
    ├── HealthConnect<Role>.kt          (Reader, Writer, Observer, Manager)
    └── HealthConnect<Role>+<Area>.kt   (one area of a role as extensions, e.g. HealthConnectReader+Aggregate.kt)

library/src/test/kotlin/com/kvs/healthconnectreporter/
├── payload/                            (SampleTest: every payload table-driven; ConversionTest: payload ⇄ Record)
├── type/                               (TypeTest: HealthType registry + value enums; ValueTypeTest)
├── service/HealthConnect<Role>Test.kt  (services against a mocked HealthConnectClient)
└── Fixtures.kt                         (one fixture per payload, allSamples, response(), asMap())

library/api/library.api                 (binary-compatibility-validator dump of the public API)
docs/adr/                               (architecture decisions, Nygard format)
docs/arc42/                             (architecture documentation; update it with every structural change)

app/                                    (Compose MVVM demo app)
└── src/main/kotlin/com/kvs/healthconnectreporter/example/
    ├── MainActivity.kt                 (binds DemoViewModel.state to DemoScreen and events back)
    ├── PermissionsRationaleActivity.kt (privacy policy screen Health Connect links to; required)
    ├── demo/                           (DemoViewModel, DemoScreen, DemoState / RowResult / DemoEvent, DemoRow)
    └── service/                        (HealthConnectReporterService and one DemoPerformer per library area)
```

### Layer Invariants
* **No client leakage**: Public API takes and returns library types (`HealthType`, `HeartRate`, `TimeRange`, `Anchor`, `ChangeSet`). Health Connect types (`androidx.health.connect.client.*`) stay `internal` behind `asOriginal()` / `from(record)`; the client is an `implementation` dependency, and `library.api` (checked by `apiCheck`) must never mention it.
* **Identity**: a payload's `id` names the stored Health Connect record (`Metadata.id`); a payload created by the consumer has `id == null`. `clientRecordId` / `clientRecordVersion` are the consumer's own key and version: `HealthConnectWriter.insert` assigns a random UUID `clientRecordId` when it is missing and returns the samples with their new `id`s (in `InsertRecordsResponse.recordIdsList` order), so update and delete work by `id` or, without one, by `clientRecordId` — never by matching values.
* **One client**: `HealthConnectReporter(context)` obtains one `HealthConnectClient` and injects it into every service. Services never call `HealthConnectClient.getOrCreate` themselves.
* **Decorators are internal glue**: `Extensions+*.kt` hold conversions and helpers; they never call the client.
* **Suspend first**: every client call is a `suspend fun` on the client's suspend API, called with the caller's coroutine context. The library never blocks a thread (`runBlocking`, `Thread.sleep`), and never picks a dispatcher for the consumer.
* **Consumer owns the manifest**: the library's manifest declares only the `<queries>` entry for the Health Connect app. The consuming app declares each `android.permission.health.*` it requests, the permissions-rationale activity and (Android 14+) the `VIEW_PERMISSION_USAGE` activity-alias; the library never merges health permissions into apps.

---

## 4. Strict Prohibitions & Bans

### Architecture & Code Bans
* ❌ **Ban on Singletons**: No `object` holding state, `companion object` instances, global `lateinit` stores or service locators. Consumers instantiate `HealthConnectReporter(context)`; dependencies are passed through constructors.
  * **Not covered** — stateless platform queries (`HealthConnectReporter.availability(context)`) and factories on payloads/types (`HeartRate.make(from:)`, `HealthType.make(from:)`, `HeartRate.collect(...)`) in `companion object`s.
* ❌ **Ban on Static-Only Utility Types**: No `object Utils` / `class Helpers` that only namespace functions. Put behavior in an extension of the type it belongs to (`Extensions+Instant.kt` → `Instant.epochMillis`).
* ❌ **Ban on Exposing Health Connect Types in Public API**: Public classes, functions, properties and typealiases never mention `HealthConnectClient`, `Record`, `Metadata`, `TimeRangeFilter`, `AggregateMetric`, `HealthPermission`, unit classes (`Mass`, `Energy`, …) or any other `androidx.health.connect.client.*` type; mapping happens in the payloads' `asOriginal()` / `from(record)` and in `decorator/`. No public `typealias` to a client type. Permissions cross the API as library `Permission`s, never as `android.permission.health.*` strings.
* ❌ **Ban on Unguarded Features**: A record type or permission behind a Health Connect feature flag (`HealthConnectFeatures.FEATURE_SKIN_TEMPERATURE`, `FEATURE_MINDFULNESS_SESSION`, `FEATURE_PLANNED_EXERCISE`, `FEATURE_READ_HEALTH_DATA_IN_BACKGROUND`, `FEATURE_READ_HEALTH_DATA_HISTORY`, …) is checked with `client.features.getFeatureStatus(...)` first; the service throws `HealthConnectException.NotAvailable("<type> needs Health Connect feature <name>")`. A type that needs a newer `connect-client` than the declared minimum (§7) is not added until the minimum is raised.
* ❌ **Ban on Ad-hoc Errors**: Throw only `HealthConnectException` subclasses with a descriptive message (`HealthConnectException.InvalidType("Invalid HealthType: $type")`). Client exceptions (`SecurityException`, `IllegalArgumentException`, `IllegalStateException`, `RemoteException`, `IOException`) are caught at the service boundary and wrapped, keeping the original as `cause`. No new exception hierarchies, no bare `Exception` / `IllegalStateException`.
* ❌ **Ban on `!!`, `error()`, `TODO()` and Unchecked Casts**: Use `?: throw HealthConnectException...`; `requireNotNull` / `require` / `check` are also banned in `main`; `as?` + throw instead of `as`.
* ❌ **Ban on Inputs the Client Rejects at Runtime**: Record constructors `require` their ranges (steps 1–1 000 000, heart rate 1–300 bpm, end after start, series samples inside the interval, …) and the client rejects unknown permissions. Validate in Kotlin before calling the client (end before start, writes to read-only types, empty permission sets, permissions the app's manifest does not declare) and throw `HealthConnectException.InvalidValue`; build records inside `clientCall { }` so any remaining `require` failure still surfaces as `InvalidValue`.
* ❌ **Ban on `else` Branches over Library Enums**: A `when` over `HealthType`, `ExerciseType`, `SleepStageType`, etc. lists every entry; no `else ->`. Client int constants that may grow (`SDK_*` statuses, `EXERCISE_TYPE_*`, `RECORDING_METHOD_*`) get an explicit fallback that throws or maps to an `UNKNOWN` entry.
* ❌ **Ban on Mutable Payloads**: Payload fields are `val`; payloads are `data class`es. Changes go through the generated `copy(...)`, Kotlin's equivalent of HealthKitReporter's `copyWith`; don't add a `copyWith`.
* ❌ **Ban on Breaking the Serialization Contract**: `Payload.make(from:)` map keys and `@SerialName`s are consumed by other runtimes (Flutter / JSON). Renaming one is a breaking change (`!` / `BREAKING CHANGE:` commit → major release).
* ❌ **Ban on Variable Units**: Health Connect values carry unit objects (`Mass`, `Energy`, `Length`, `Velocity`, `Power`, `Pressure`, `Temperature`, `Volume`, `BloodGlucose`, `Percentage`). Each payload field holds one canonical unit — kg, kcal, m, m/s, W, mmHg, °C, L, mmol/L, percent 0–100 — named in its KDoc and converted only in `decorator/Extensions+Units.kt`. Only `Aggregation.unit` / `Aggregate.unit` carry a unit string. Durations are milliseconds (`…Millis`), instants epoch milliseconds (`…Timestamp`), zone offsets seconds (`…ZoneOffsetSeconds`).
* ❌ **Ban on Gson in the Library API**: Payloads use `kotlinx.serialization`; `json` / `encoded()` come from the `Payload` interface.
* ❌ **Ban on Medical Records Until Decided**: Personal health records (`MedicalResource`, FHIR, `FEATURE_PERSONAL_HEALTH_RECORD`) need their own ADR before any code; they are out of scope for 1.x.

### Git & VCS Bans
* ❌ **Ban on `git commit --no-verify`**: Bypassing pre-commit git hooks, static analysis, or test suites is forbidden.
* ❌ **Ban on Blind Staging (`git add .` / `git add -A`)**: Run `git status` first and stage only relevant source, test, and config files explicitly.
* ❌ **Ban on Direct Force Pushing**: Force pushing to `master` is forbidden. Use `--force-with-lease` on isolated feature branches only when necessary.
* ❌ **Ban on Single-Line Shortcut Commits**: Omitting the detailed description, `Changes:`, and `Tests:` sections in commit messages is strictly forbidden.
* ❌ **Ban on AI Attribution Trailers**: A commit message MUST NEVER carry `Co-Authored-By: <Model Name> <noreply@anthropic.com>`, or any other trailer crediting an AI model or tool.
* ❌ **Ban on Committing User or Build State**: Never commit `local.properties`, `build/`, `.gradle/`, `*.iml`, `.idea/workspace.xml`, keystores, or `google-services.json`.

---

## 5. Code Style & Layer Specifications

### A. File Layout
* **Header**: every Kotlin file starts with the package declaration, then imports (no wildcard imports), then declarations. No author/date header block. Client types whose simple name clashes with a library type are imported with an `Original` alias (`import androidx.health.connect.client.records.metadata.Device as OriginalDevice`).
* **Indentation**: 4 spaces. Line length ≤ 120 in `library/` and `app/` (`.editorconfig`, ktlint).
* **Wrapping**: once a declaration or call doesn't fit on one line, put **every** argument on its own line, a trailing comma after the last one, and the closing `)` on its own line. Builder chains put each `.call()` on its own line.
* **Sections**: the client conversion of a writable payload sits between `// region Original` and `// endregion`; dispatch over the sealed hierarchy lives in `decorator/` (`Extensions+WritableSample.kt`, `Extensions+HealthType.kt`).
* **Access Control**: `public` (implicit) only for consumer-facing API; client-backed constructors and plumbing (`asOriginal`, `from(record)`, unit conversions) are `internal`. Stored service dependencies are `private val`. `explicitApi()` is on in `library/build.gradle.kts`.

### B. Documentation Comments
Every public declaration carries KDoc in this format — type names in **bold**, defaults spelled out, thrown exceptions listed:

```kotlin
/**
 * Reads heart rate records.
 *
 * @param range **TimeRange** time range to read
 * @param ordering **Ordering** sort by start time. [Ordering.DESCENDING] by default
 * @param limit maximum number of records. No limit by default
 * @return **List<HeartRate>** the stored records, empty when there is no data
 * @throws HealthConnectException.InvalidValue when `range.endTimestamp` is before `range.startTimestamp`
 * @throws HealthConnectException.NotAuthorized when read permission is missing
 */
```

Short one-liners use `/** **HealthConnectWriter** class for Health Connect writing operations */`.

### C. Types (`model/type/`)
* `HealthType` is a `@Serializable enum class` with one entry per Health Connect record class (`StepsRecord` → `STEPS`): `identifier` (stable snake_case library id, equal to SamsungHealthReporter's where the concept matches), `isWritable`, `feature` (the `Feature` it needs, or `null`), `aggregations`, and `make(from:)` throwing `HealthConnectException.InvalidType`. Every Health Connect record type is readable, writable (by the app that requests write permission) and observable, so there are no per-type read/observe flags. It has no client import.
* The client side lives in `decorator/Extensions+HealthType.kt`: `recordClass`, `readPermission`, `writePermission` (`HealthPermission.getReadPermission(recordClass)`) and `sample(record)` — exhaustive `when`s listing every entry. `TypeTest` checks that the registry covers every entry and maps both ways.
* `Aggregation` lists every `AggregateMetric` the client exposes on a record class (`StepsRecord.COUNT_TOTAL` → `STEPS_COUNT_TOTAL`) with its `healthType` and `unit`; `HealthConnectReader+Aggregate.kt` maps each entry to its metric.
* `Permission` is either a data permission (`HealthType` + `AccessType.READ` / `WRITE`) or a feature permission (`READ_IN_BACKGROUND`, `READ_HISTORY`); `Extensions+Permission.kt` maps both ways to permission strings.
* Value enums mirror a client constant family entry for entry and by name (`ExerciseType` ↔ `EXERCISE_TYPE_*`, `SleepStageType` ↔ `STAGE_TYPE_*`, `MealType`, `RecordingMethod`, `DeviceType`, …), converted through one `Map<Int, Entry>` per enum; `TypeTest.roundTrip` proves every entry maps both ways.
* Adding an entry means updating **every** exhaustive `when` over it (the compiler lists them), the fixtures, the Example app manifest permissions and the `README.md` type table.

### D. Payloads (`model/payload/`)
A sample payload is a `@Serializable @SerialName("<identifier>") data class` implementing `Sample` (every Health Connect record is writable, so in practice `WritableSample`). Payloads follow `HeartRate.kt` exactly:
1. Constructor order: `startTimestamp: Long`, `endTimestamp: Long` (or `Long? = null` for instantaneous records), `harmonized: Harmonized`, then `id`, `clientRecordId`, `clientRecordVersion`, `startZoneOffsetSeconds`, `endZoneOffsetSeconds`, `recordingMethod`, `device`, `dataOrigin`, `lastModifiedTimestamp`, all `= null` (`recordingMethod` defaults to `RecordingMethod.MANUAL_ENTRY` when written).
2. `init { validateInterval() }`; nested series/session classes validate their own intervals and that they lie inside the parent's.
3. `override val healthType get() = HealthType.<ENTRY>` (no backing field, so not serialized) and `override val json get() = encoded` — sample JSON carries the `type` discriminator, so `Sample.serializer()` decodes any of them.
4. Nested `@Serializable data class Harmonized` with the values (and nested `Series` / `Stage` / `Lap` / `Segment` / `Route` classes); KDoc names each field's unit and range.
5. `// region Original`: `internal fun asOriginal(): <Name>Record = <Name>Record(…, metadata = metadata())`, where `metadata()` (in `Extensions+WritableSample.kt`) picks the right `Metadata.manualEntry` / `autoRecorded` / `activelyRecorded` / `…WithId` factory from `recordingMethod`, `device`, `id` and `clientRecordId`; register the type in `Extensions+WritableSample.kt`.
6. `public companion object : Payload.Factory<Name>({ Name.serializer() })` — `make(from:)`, `collect(from:)` and `decode(json)` come from the factory; add only `internal fun from(record: <Name>Record): Name`.

`Device`, `Aggregate`, `ChangeSet`, `Anchor`, `Page` and `TimeRange` are plain `Payload`s with a factory. Flat payloads (one `Harmonized` of scalar fields) can be regenerated from a field table; keep them byte-identical in shape.

KDoc is required on every public type, constructor, function and interface.

### E. Decorators (`decorator/`)
* One file per extended type: `Extensions+<TypeName>.kt`.
* Extensions hold conversions (`TimeRange.asTimeRangeFilter`, `Ordering.ascending`, `AggregationResult.harmonize`), the type registry, unit conversion, and `clientCall { }`, which rethrows client exceptions as `HealthConnectException` (`SecurityException` → `NotAuthorized`, `IllegalArgumentException` → `InvalidValue`, `UnsupportedOperationException` → `NotAvailable`, `RemoteException` / `IOException` / `IllegalStateException` → `Platform`).
* Pure conversions are extension properties (`asInstant`, `epochMillis`, `kilograms`, `asOriginal`).

### F. Services (`service/`)
* `class HealthConnect<Role> internal constructor(private val client: HealthConnectClient)`; `internal val client` when `HealthConnect<Role>+<Area>.kt` extensions share it. Test seams (`now`, `makeClientRecordId`) are extra `internal` constructor parameters with defaults.
* Every client call **and the request/record building before it** goes through `clientCall { }`. Validate input first and throw before calling the client.
* **Reader**: `read(type, range, ordering = DESCENDING, limit = null, origins = emptySet())` follows page tokens until `limit`; `readPage(type, range, pageSize = 1000, pageToken = null, ordering, origins)` (page size 1–5000); `read(type, id)` for one record; `aggregate(aggregation, range, origins)`, `aggregate(aggregations, range, group: TimeGroup, origins)` in `HealthConnectReader+Aggregate.kt` — `TimeGroup.Duration` → `aggregateGroupByDuration`, `TimeGroup.Period(unit, count, zoneId)` → `aggregateGroupByPeriod` over the range converted to local time in the caller's `zoneId`. Without `Permission.READ_HISTORY`, Health Connect returns only data from 30 days before the first grant; the KDoc says so.
* **Writer**: `insert` (list or one; one call per batch, ≤ 1000 records; assigns missing client record ids), `update` (by `id`, else `clientRecordId`, else `InvalidValue`), `delete(type, ids, clientRecordIds)`, `delete(type, range)`, `delete(sample)`.
* **Observer**: `anchor(types): Anchor` wraps `getChangesToken`; `changes(anchor): ChangeSet` follows `hasMore` and returns upserted samples, deleted ids and the next `Anchor`; an expired token throws `HealthConnectException.AnchorExpired`. `observe(anchor, interval = 1.minutes)` is a cold polling `Flow<ChangeSet>` that emits only non-empty sets. Background delivery (WorkManager + `READ_IN_BACKGROUND`) is the consumer's job; the README shows how.
* **Manager**: `permissionContract()` returns a library-typed `ActivityResultContract<Set<Permission>, Set<Permission>>`; `requestPermissions(activity: ComponentActivity, permissions)` suspends on it through `activityResultRegistry`; `grantedPermissions()`; `revokeAllPermissions()`; `isFeatureAvailable(feature)`; `settingsIntent()` / `installIntent()` (Play Store page when `availability == UPDATE_REQUIRED`).
* Empty results mean "no data"; records that can't be converted are skipped (`collect`), never reported as empty.

### G. Example App (`app/`)
* Jetpack Compose + Material 3, single `MainActivity` that binds `DemoViewModel.state` (`StateFlow<DemoState>`) to `DemoScreen` and its events back to `DemoViewModel.onEvent(DemoEvent)`. Composables render state and forward events; they never touch the library.
* `DemoViewModel` runs each tapped row in `viewModelScope` (one `Job` per row; tapping a running row stops it) and stores a `RowResult` (`Running`, `Success`, `Failure`) per row id.
* `HealthConnectReporterService` creates the reporter lazily (connecting may throw `NotAvailable`), exposes `sections` and `publisher(row, activity): Flow<String>`, and owns one `DemoPerformer` per library area (`ManagerDemos`, `ReaderDemos`, `WriterDemos`, `ObserverDemos`), each keeping its state (inserted samples, anchors) `private`.
* Rows are generated from `HealthType.entries` / `Aggregation.entries` (a read, insert and changes row per type, an aggregate row per aggregation); permissions request read and write for every type.
* `app/src/main/AndroidManifest.xml` declares `android.permission.health.READ_*` / `WRITE_*` for every `HealthType`, the feature permissions, `PermissionsRationaleActivity` (`androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE`) and the `VIEW_PERMISSION_USAGE` activity-alias; a `ManifestTest`-style check or the demo's permission row proves they match `HealthType.entries`.
* Every new public library method gets a `DemoRow` in the matching performer and a usage snippet in `README.md`.

---

## 6. Testing Strategy & Execution Protocol

The codebase enforces test-first **TDD**. Code without tests will be rejected.

### A. Testing Categories & Toolstack

| Category | Target | Package | Purpose & Scope |
| :--- | :--- | :--- | :--- |
| **Payload Tests** | `model/payload/*`, `model/*` | JUnit 4 + kotlin-test | Every payload (table-driven over `allSamples`): `json` → `Sample.serializer()` round trip, `type` discriminator, `make(from:)` from a Flutter-shaped map, missing / mistyped keys → `InvalidValue`, `collect` skipping invalid maps. |
| **Conversion Tests** | `decorator/*` | JUnit 4 + connect-client | Every payload → `Record` → payload, metadata (id, client record id/version, recording method, device) and units included. Records are plain Kotlin classes and build on the JVM. |
| **Type Tests** | `model/type/*`, registry | JUnit 4 | `make(from:)`, registry ↔ record classes ↔ permission strings, value enums ↔ client constants for every entry. |
| **Service Tests** | `service/*`, facade | MockK + `kotlinx-coroutines-test` | Public calls against a mocked `HealthConnectClient`: paging, limits, validation before any client call, error wrapping, feature gating, observer flow on virtual time. (`connect-testing`'s `FakeHealthConnectClient` pins an alpha `connect-client`; don't add it until it targets a stable one.) |
| **Example App** | `app/` | manual, device / emulator | Health Connect runs on an Android 14+ emulator; insert data with the demo or the Health Connect toolbox, then run the matching rows. |

### B. Test Conventions
* Name the object under test `sut`. Test names describe the flow in backticks: `` `create then encode then decode` ``, `` `read stops at the limit` ``.
* Use the fixed fixtures in `Fixtures.kt` (`START = 1_626_884_800_000L`); a new payload adds a fixture to `allSamples`, which `SampleTest` checks covers every `HealthType`.
* Mock client responses with `response(records, pageToken)`; build records only through their public constructors and `Metadata` factories.
* No `!!`, no swallowing `try/catch`; use `assertFailsWith<HealthConnectException.InvalidValue>`.

### C. Official CLI Commands

```bash
# 1. Unit tests with coverage (Kover); report in library/build/reports/kover
./gradlew :library:testDebugUnitTest :library:koverXmlReport :library:koverVerify

# 2. Lint and static analysis — every violation fails
./gradlew ktlintCheck detekt :library:lintDebug

# 3. Public API dump check (binary-compatibility-validator); after a deliberate change: ./gradlew :library:apiDump
./gradlew :library:apiCheck

# 4. Build the library AAR and the example app
./gradlew :library:assembleRelease :app:assembleDebug
```

`.github/workflows/ci.yml` runs all four on every PR and push to `master`, plus a version/changelog guard.
There is no lint baseline. An inline `@Suppress` carries a comment with its reason.

### D. Test-First TDD Pipeline
1. **Coverage**: every payload, type, conversion and `make(from:)` path is covered. Behavior that needs a live Health Connect (permission UI, real data, feature availability on a device) is checked through the Example app.
2. **Cycle**:
  * 🔴 **Red**: Write a failing test defining the specification *before* production code.
  * 🟢 **Green**: Write minimal production code to pass the test.
  * 🔵 **Refactor**: Clean up implementation details while ensuring tests stay green.

### E. Quality Gate Requirements
Before any commit or PR creation, the codebase must pass all gates:
1. `ktlintCheck` + `detekt` + Android lint — **zero warnings or errors**.
2. `testDebugUnitTest` — **all tests green**; quote the executed/failed counts it prints.
3. `apiCheck` — passes; a deliberate public API change ships its regenerated `library/api/library.api` (`./gradlew apiDump`).
4. Example app builds — **required whenever public API changes**. Otherwise state "not applicable — no public API change" in the evidence line rather than omitting it: an unstated gate reads as a skipped one.
5. Coverage — `koverVerify` passes the minimum line and branch coverage in `library/build.gradle.kts` (`minBound(100, LINE)` and `minBound(100, BRANCH)` as of 10.10.2026, measured on the bootstrap); quote the measured percentages. A PR may lower a bound only for kotlinx.serialization-generated branches it can't reach, naming them in the PR; otherwise never lower them, and raise them to the new measured level (rounded down) when tests add coverage.

---

## 7. Release & Versioning

* **Platform floor**: `minSdk 26`, `compileSdk` / `targetSdk` 36, JVM target 17, `androidx.health.connect:connect-client` `1.1.0`+. Raising any floor is a breaking change.
* **Before tagging**: run the Example app against Health Connect on a device or Android 14+ emulator.
* **SemVer**: breaking public API or serialization-contract changes → major; new types/features → minor; fixes → patch. The first complete release (all four services) ships as `1.0.0` (`Release-As: 1.0.0` footer); until then nothing is released.
* Releases are automated by release-please (`.github/workflows/release.yml`, `release-please-config.json`). It derives the bump from Conventional Commits on `master` and keeps a `chore: release X.Y.Z` PR open that bumps `.release-please-manifest.json`, `VERSION_NAME` in `gradle.properties`, prepends the `## [X.Y.Z] - dd.MM.yyyy.` entry to `CHANGELOG.md` and updates the `x-release-please-version` line in `README.md`.
* Merging the release PR creates the bare tag `X.Y.Z` (no `v` prefix) and the GitHub Release, then `release.yml` dispatches `publish.yml` on the tag, which builds it on JitPack (`jitpack.yml`) and fails when the build or the AAR is missing. The release workflow also rewrites release-please's changelog headers to `## [X.Y.Z] - dd.MM.yyyy.` on the release PR. Never tag, bump versions or edit released `CHANGELOG.md` entries by hand.
* Commit messages are the changelog: write the summary for consumers.
* New public API is documented in `README.md` with a usage snippet; the README states the manifest entries the consumer must add and that Google Play requires a Health Connect permissions declaration.

---

## 8. Git & GitHub CLI (`gh`) Operational Protocols

### Branch Naming Convention
All branches MUST follow the strict user initials and issue structure:

```text
<initials>/issue-<XXX>
```

* **Example**: `vk/issue-14` or `ab/issue-102`

### GitHub CLI (`gh`) Operations

```bash
# View assigned issue context
gh issue view <issue_number>

# Create feature branch for issue (ALWAYS branch off a freshly pulled master)
git fetch origin
git switch master && git pull --ff-only origin master
git checkout -b <initials>/issue-<issue_number>

# Create Pull Request using gh CLI
gh pr create \
  --title "<type>(<scope>)[!]: <short summary>" \
  --body "## Summary
<description>

## Changes
- <file_path>: <details>

## Tests
- Summary: unit tests green, ktlint/detekt/lint clean, apiCheck + Example build passing.

Refs: #<issue>"

# Check PR checks and review status
gh pr status
gh pr checks
```

### Mandatory Git Execution Sequence
Before committing or creating a PR, run this exact sequence:

```bash
# 1. Lint
./gradlew ktlintCheck detekt :library:lintDebug

# 2. Execute test suite with coverage
./gradlew :library:testDebugUnitTest :library:koverVerify

# 3. API check, then build the Example app (public API changes)
./gradlew :library:apiCheck :app:assembleDebug

# 4. Inspect file status before staging
git status

# 5. Stage specific changed files intentionally (NO blind `git add .`)
git add library/src/main/kotlin/com/kvs/healthconnectreporter/model/payload/<Name>.kt \
  library/src/test/kotlin/com/kvs/healthconnectreporter/payload/SampleTest.kt

# 6. Commit using strict multi-paragraph format
git commit -m "<type>(<scope>)[!]: <short summary>" \
  -m "<longer description / context>" \
  -m "Changes:
- <file_path>: <details>
- <file_path>: <details>" \
  -m "Tests: <summary>" \
  -m "Refs: #<issue>"
```

### Commit Format Specification
```text
<type>(<scope>)[!]: <short summary>

<longer description / context>

Changes:
- <file path>: <details>
- <file path>: <details>

Tests: <summary>

[BREAKING CHANGE: <what breaks and how to migrate>]
Refs: #<issue>
```

* The header is a [Conventional Commit](https://www.conventionalcommits.org) — release-please parses it to pick the version bump and changelog section; a header in any other shape is silently left out of the release.
* `<type>`: `feat` (→ minor), `fix` (→ patch), or `docs`, `test`, `refactor`, `ci`, `build`, `chore` (no release). `!` after the scope or a `BREAKING CHANGE:` footer → major.
* `Refs: #<issue>` is required whenever an issue exists.

### Local Issues
Work not yet on GitHub lives in `ISSUE-<slug>.md` files at the repo root, titled with the Conventional Commit header the work will ship under. They are for local use only: git-ignored, never committed or referenced from committed files. File one on GitHub with the `github-create` skill (or `gh issue create --body-file ISSUE-<slug>.md`) when it should become public.

---

## 9. AI Verification Checklist

Before outputting code or submitting PRs, explicitly verify:
* [ ] Does every new file live under `library/src/main/kotlin/com/kvs/healthconnectreporter/{decorator,model,service}` and match the sibling files' style?
* [ ] Are singletons, global state and static-only utility objects absent, with `HealthConnectClient` injected via the constructor?
* [ ] Is new public API free of Health Connect types (no `androidx.health.connect.client.*` in `library.api`), with mapping in `asOriginal()` / `from(record)`, and does `apiCheck` pass?
* [ ] Is every client call `suspend` and inside `clientCall { }`, with no `runBlocking` or thread blocking?
* [ ] Do update/delete act on stored records by `id` or `clientRecordId`, and is every input the client rejects validated in Kotlin first?
* [ ] Are feature-gated types and permissions checked with `getFeatureStatus` and reported as `NotAvailable`?
* [ ] Are errors thrown only as `HealthConnectException` subclasses with descriptive messages and the client exception as `cause`, without `!!`, `error()`, `require` or unchecked casts?
* [ ] Does every new payload follow `HeartRate.kt`: `@Serializable @SerialName` data class, `Harmonized`, `val` fields in canonical units, `validateInterval()`, `json = encoded`, `// region Original`, `Payload.Factory` companion with `internal fun from(record)`, and registration in the `decorator/` dispatchers?
* [ ] Are new enum entries handled in every exhaustive `when`, with no `else ->` over library enums?
* [ ] Are `make(from:)` keys, `@SerialName`s and `HealthType.identifier`s unchanged, or is the break versioned as major?
* [ ] Does every public declaration carry KDoc in the `@param` / `@return` / `@throws` format?
* [ ] Are wrapped calls one-argument-per-line with trailing commas, and lines ≤ 120?
* [ ] Is the new payload in `Fixtures.allSamples`, so the table-driven round-trip tests cover it, and were they red first?
* [ ] Did lint, unit tests, `koverVerify`, `apiCheck` and the Example app build pass?
* [ ] Does the library manifest still declare no health permissions, and does the Example app manifest declare every new one?
* [ ] Were `README.md` and the Example app updated for user-visible changes, leaving `CHANGELOG.md` and versions to release-please?
* [ ] Does every new public method or type entry have a demo in the Example app (a `DemoRow` for methods, `entries`-driven permissions for types)?
* [ ] Is the branch named strictly `<initials>/issue-<XXX>`?
* [ ] Are git commits made without `--no-verify` and staged without blind `git add .`?
* [ ] Was `gh pr create` used with structured title/body matching commit specs?
* [ ] Does the commit message carry the Conventional `<type>(<scope>)[!]: <summary>` header, a description, `Changes:`, `Tests:` and `Refs: #<issue>` (§8), with no AI trailer?
