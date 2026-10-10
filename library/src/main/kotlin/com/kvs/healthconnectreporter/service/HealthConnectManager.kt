package com.kvs.healthconnectreporter.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.asPermissions
import com.kvs.healthconnectreporter.decorator.checkDeclared
import com.kvs.healthconnectreporter.decorator.clientCall
import com.kvs.healthconnectreporter.decorator.feature
import com.kvs.healthconnectreporter.model.Permission
import com.kvs.healthconnectreporter.model.type.Feature
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume

/** **HealthConnectManager** class for Health Connect permissions, features and screens */
public class HealthConnectManager internal constructor(
    private val client: HealthConnectClient,
    private val contract: () -> ActivityResultContract<Set<Permission>, Set<Permission>> = { PermissionContract() },
) {
    /**
     * The permission request contract, for `registerForActivityResult` in your activity or fragment.
     *
     * Launching it throws **HealthConnectException.InvalidValue** when the permissions are empty or missing from the
     * app manifest.
     *
     * @return **ActivityResultContract<Set<Permission>, Set<Permission>>** permissions to request in, granted
     * permissions out
     */
    public fun permissionContract(): ActivityResultContract<Set<Permission>, Set<Permission>> = contract()

    /**
     * Shows the Health Connect permission screen and waits for the user's choice.
     *
     * @param activity **ComponentActivity** the activity that shows the screen
     * @param permissions **Set<Permission>** the permissions to request, each declared in the app manifest
     * @return **Set<Permission>** the permissions granted afterwards
     * @throws HealthConnectException.InvalidValue when [permissions] is empty or missing from the app manifest
     * @throws HealthConnectException.NotAvailable when a permission needs a Health Connect feature the device lacks
     */
    public suspend fun requestPermissions(
        activity: ComponentActivity,
        permissions: Set<Permission>,
    ): Set<Permission> {
        activity.checkDeclared(permissions)
        permissions.mapNotNullTo(mutableSetOf()) { it.feature }.forEach { feature ->
            if (!client.isAvailable(feature)) {
                throw HealthConnectException.NotAvailable("Permission needs Health Connect feature $feature")
            }
        }
        return suspendCancellableCoroutine { continuation ->
            val launcher =
                activity.activityResultRegistry.register(
                    "health_connect_permissions_${UUID.randomUUID()}",
                    permissionContract(),
                ) { granted -> continuation.resume(granted) }
            continuation.invokeOnCancellation { launcher.unregister() }
            launcher.launch(permissions)
        }
    }

    /**
     * Reads the permissions the user granted this app.
     *
     * @return **Set<Permission>** the granted permissions
     * @throws HealthConnectException.Platform when Health Connect can't be reached
     */
    public suspend fun grantedPermissions(): Set<Permission> =
        clientCall { client.permissionController.getGrantedPermissions() }.asPermissions

    /**
     * Revokes every permission the user granted this app.
     *
     * @throws HealthConnectException.Platform when Health Connect can't be reached
     */
    public suspend fun revokeAllPermissions() {
        clientCall { client.permissionController.revokeAllPermissions() }
    }

    /**
     * Checks whether the Health Connect on this device supports a feature.
     *
     * @param feature **Feature** the feature
     * @return **Boolean** true when it is available
     */
    public suspend fun isFeatureAvailable(feature: Feature): Boolean = client.isAvailable(feature)

    /**
     * An intent that opens Health Connect's data management screen, or its settings on older versions.
     *
     * @param context **Context** any context
     * @return **Intent** start it with `startActivity`
     */
    public fun settingsIntent(context: Context): Intent = HealthConnectClient.getHealthConnectManageDataIntent(context)

    /**
     * An intent that opens the Health Connect app on Google Play, for installing or updating it when
     * **HealthConnectReporter.availability** is **Availability.UPDATE_REQUIRED**.
     *
     * @param context **Context** any context
     * @return **Intent** start it with `startActivity`
     */
    @Suppress("UseKtx") // core-ktx 1.19 would force compileSdk 37 on consumers; Uri.parse is the same call
    public fun installIntent(context: Context): Intent =
        Intent(Intent.ACTION_VIEW)
            .setPackage(PLAY_STORE_PACKAGE)
            .setData(Uri.parse("market://details?id=$HEALTH_CONNECT_PACKAGE&url=healthconnect%3A%2F%2Fonboarding"))
            .putExtra("overlay", true)
            .putExtra("callerId", context.packageName)

    internal companion object {
        const val PLAY_STORE_PACKAGE = "com.android.vending"
        const val HEALTH_CONNECT_PACKAGE = "com.google.android.apps.healthdata"
    }
}
