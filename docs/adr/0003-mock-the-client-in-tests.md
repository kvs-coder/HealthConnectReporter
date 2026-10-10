# 3. Test services against a mocked HealthConnectClient

Date: 10.10.2026

## Status

Accepted

## Context

`androidx.health.connect:connect-testing` offers `FakeHealthConnectClient`, but its latest version (1.0.0-alpha04) pins `connect-client` to exactly `1.2.0-alpha05`, which would pull an alpha client into the tests and diverge from the 1.1.0 the library ships against.

## Decision

Service tests mock the `HealthConnectClient` interface with MockK and build responses from the client's public record constructors. Revisit when `connect-testing` targets a stable client.

## Consequences

- Tests state every client interaction explicitly; paging and `hasMore` loops are stubbed per test.
