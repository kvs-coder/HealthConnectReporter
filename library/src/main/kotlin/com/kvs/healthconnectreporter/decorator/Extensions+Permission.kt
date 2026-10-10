package com.kvs.healthconnectreporter.decorator

import android.content.Context
import android.content.pm.PackageManager
import androidx.health.connect.client.permission.HealthPermission
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.model.Permission
import com.kvs.healthconnectreporter.model.type.AccessType
import com.kvs.healthconnectreporter.model.type.Feature
import com.kvs.healthconnectreporter.model.type.HealthType

private val allPermissions: List<Permission> =
    Permission.of(HealthType.entries).toList() +
        listOf(Permission.ReadInBackground, Permission.ReadHistory, Permission.WriteExerciseRoute)

/** **String** the Health Connect permission string */
internal val Permission.asOriginal: String
    get() =
        when (this) {
            is Permission.Data ->
                when (access) {
                    AccessType.READ -> healthType.readPermission
                    AccessType.WRITE -> healthType.writePermission
                }
            Permission.ReadInBackground -> HealthPermission.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
            Permission.ReadHistory -> HealthPermission.PERMISSION_READ_HEALTH_DATA_HISTORY
            Permission.WriteExerciseRoute -> HealthPermission.PERMISSION_WRITE_EXERCISE_ROUTE
        }

/** **Feature?** the Health Connect feature the permission needs */
internal val Permission.feature: Feature?
    get() =
        when (this) {
            is Permission.Data -> healthType.feature
            Permission.ReadInBackground -> Feature.READ_HEALTH_DATA_IN_BACKGROUND
            Permission.ReadHistory -> Feature.READ_HEALTH_DATA_HISTORY
            Permission.WriteExerciseRoute -> null
        }

internal val Set<Permission>.asOriginal: Set<String>
    get() = mapTo(mutableSetOf()) { it.asOriginal }

/** The library permissions granted by Health Connect permission strings; a shared string grants every type. */
internal val Set<String>.asPermissions: Set<Permission>
    get() = allPermissions.filterTo(mutableSetOf()) { it.asOriginal in this }

/**
 * Throws when [permissions] is empty or names a permission this app's manifest does not declare, which
 * Health Connect would ignore.
 */
internal fun Context.checkDeclared(permissions: Set<Permission>) {
    if (permissions.isEmpty()) throw HealthConnectException.InvalidValue("Invalid permissions: empty")
    @Suppress("DEPRECATION") // the PackageInfoFlags overload needs API 33; minSdk is 26
    val declared =
        packageManager
            .getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            .requestedPermissions
            .orEmpty()
            .toSet()
    val missing = permissions.asOriginal - declared
    if (missing.isNotEmpty()) {
        throw HealthConnectException.InvalidValue("Permissions missing from the app manifest: ${missing.sorted()}")
    }
}
