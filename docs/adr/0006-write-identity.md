# 6. Write identity: client record id before id

Date: 10.10.2026

## Status

Accepted

## Context

AGENTS.md §3 says update and delete act "by `id` or, without one, by `clientRecordId`". Health Connect's `Metadata.…WithId` factories, the only way to write a record id, are documented "only when there's no client ID or version associated with the record"; using them for a record with a client id drops that id.

## Decision

- `metadata()` uses the client id factory (with `clientRecordVersion`) when a sample has a `clientRecordId`, else the `…WithId` factory when it has an `id`, else the plain factory.
- `HealthConnectWriter.update` upserts samples with a `clientRecordId` through `insertRecords`, where the highest `clientRecordVersion` wins, and updates the others by `id` through `updateRecords`; a sample with neither is an `InvalidValue`.
- `delete(sample)` deletes by `id` when there is one, else by `clientRecordId`.

## Consequences

- A consumer that keys records by `clientRecordId` keeps that key across updates, and bumps `clientRecordVersion` to make an update win.
- Records without a client id are updated in place by their Health Connect `id`.
