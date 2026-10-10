# 2. Architecture Constraints

- `minSdk 26`, `compileSdk` / `targetSdk` 36, JVM 17, `connect-client` 1.1.0.
- Health Connect is a system module on Android 14+ and a Play app on Android 9–13; it is unavailable below.
- Apps can read only 30 days before their first grant without the history permission, and only in the foreground
  without the background permission.
- Distribution through JitPack from bare `X.Y.Z` tags created by release-please.
