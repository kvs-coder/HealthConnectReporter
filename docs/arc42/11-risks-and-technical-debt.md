# 11. Risks and Technical Debt

| Risk | Mitigation |
| :--- | :--- |
| `connect-client` 1.2 changes record or metadata APIs | Pin 1.1.0; conversion tests catch breaks on upgrade. |
| Changes tokens expire after 30 days | `HealthConnectException.AnchorExpired` tells the consumer to re-read in full. |
| Rate limiting on frequent reads | Observer polls at ≥ 1 minute by default; errors surface as `Platform`. |
| Name clash with the platform `android.health.connect.HealthConnectException` | Library package differs; documented in README. |
