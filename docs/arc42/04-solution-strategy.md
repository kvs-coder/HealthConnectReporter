# 4. Solution Strategy and Building Blocks

| Building block | Package | Responsibility |
| :--- | :--- | :--- |
| `HealthConnectReporter` | root | Availability check, creates the one `HealthConnectClient`, exposes the services. |
| `HealthConnectException` | root | The single sealed error hierarchy. |
| Services | `service/` | Reader, Writer, Observer, Manager — the only callers of the client. |
| Types | `model/type/` | `HealthType`, `Aggregation`, value enums; no client imports. |
| Payloads | `model/payload/`, `model/` | `@Serializable` data classes and value types. |
| Decorators | `decorator/` | Conversions between payloads and records, units, error wrapping. |

Current state: the facade, `Availability`, `HealthConnectException`, `HealthType` (41 record types), `Aggregation`, the value enums one payload per record and `HealthConnectReader` (read, page, read by id, aggregate) and `HealthConnectWriter` (insert, update, delete) and `HealthConnectObserver` (anchor, changes, polling flow) exist; the manager is planned.
