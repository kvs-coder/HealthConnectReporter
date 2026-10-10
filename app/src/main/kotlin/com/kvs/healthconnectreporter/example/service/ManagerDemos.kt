package com.kvs.healthconnectreporter.example.service

import android.content.Context
import androidx.activity.ComponentActivity
import com.kvs.healthconnectreporter.HealthConnectReporter
import com.kvs.healthconnectreporter.example.demo.DemoRow
import com.kvs.healthconnectreporter.example.demo.DemoSection
import com.kvs.healthconnectreporter.model.Permission
import com.kvs.healthconnectreporter.model.type.Feature
import com.kvs.healthconnectreporter.model.type.HealthType
import kotlinx.coroutines.flow.Flow

/** Demos of **HealthConnectManager** and the reporter's availability check. */
class ManagerDemos(
    private val context: Context,
    private val reporter: () -> HealthConnectReporter,
) : DemoPerformer {
    private val demos =
        linkedMapOf<DemoRow, suspend (ComponentActivity) -> String>(
            DemoRow("manager.availability", "Health Connect availability", "HealthConnectReporter.availability") to {
                HealthConnectReporter.availability(context).name
            },
            DemoRow("manager.request", "Request every permission", "manager.requestPermissions") to { activity ->
                val manager = reporter().manager
                val types =
                    HealthType.entries.filter { type ->
                        type.feature?.let { manager.isFeatureAvailable(it) }
                            ?: true
                    }
                val granted = manager.requestPermissions(activity, Permission.of(types) + Permission.WriteExerciseRoute)
                "Granted ${granted.size} permissions:\n" + granted.joinToString("\n")
            },
            DemoRow(
                "manager.requestFeatures",
                "Request reading history and in the background",
                "manager.requestPermissions(ReadHistory, ReadInBackground)",
            ) to { activity ->
                reporter()
                    .manager
                    .requestPermissions(activity, setOf(Permission.ReadHistory, Permission.ReadInBackground))
                    .joinToString("\n")
                    .ifEmpty { "Nothing granted" }
            },
            DemoRow("manager.granted", "Granted permissions", "manager.grantedPermissions") to {
                val granted = reporter().manager.grantedPermissions()
                "${granted.size} granted:\n" + granted.joinToString("\n")
            },
            DemoRow("manager.features", "Feature availability", "manager.isFeatureAvailable") to {
                Feature.entries.map { "$it: ${reporter().manager.isFeatureAvailable(it)}" }.joinToString("\n")
            },
            DemoRow("manager.settings", "Open Health Connect", "manager.settingsIntent") to { activity ->
                activity.startActivity(reporter().manager.settingsIntent(activity))
                "Opened"
            },
            DemoRow("manager.install", "Open Health Connect on Google Play", "manager.installIntent") to { activity ->
                activity.startActivity(reporter().manager.installIntent(activity))
                "Opened"
            },
            DemoRow("manager.revoke", "Revoke every permission", "manager.revokeAllPermissions") to {
                reporter().manager.revokeAllPermissions()
                "Revoked; Health Connect applies it when the app restarts"
            },
        )

    override val section = DemoSection("Manager", demos.keys.toList())

    override fun perform(
        row: DemoRow,
        activity: ComponentActivity,
    ): Flow<String> = oneShot { demos.getValue(row)(activity) }
}
