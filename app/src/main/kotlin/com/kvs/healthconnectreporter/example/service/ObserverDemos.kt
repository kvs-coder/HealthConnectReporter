package com.kvs.healthconnectreporter.example.service

import androidx.activity.ComponentActivity
import com.kvs.healthconnectreporter.HealthConnectReporter
import com.kvs.healthconnectreporter.example.demo.DemoRow
import com.kvs.healthconnectreporter.example.demo.DemoSection
import com.kvs.healthconnectreporter.model.Anchor
import com.kvs.healthconnectreporter.model.ChangeSet
import com.kvs.healthconnectreporter.model.type.HealthType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.time.Duration.Companion.seconds

/** Demos of **HealthConnectObserver**: changes per type since the last tap, and a polling flow. */
class ObserverDemos(
    private val reporter: () -> HealthConnectReporter,
) : DemoPerformer {
    /** The anchor per type; a real app persists **Anchor.json**. */
    private val anchors = mutableMapOf<HealthType, Anchor>()

    private val observe =
        DemoRow("observer.observe", "Observe steps every 10 s (tap again to stop)", "observer.observe")
    private val changeRows =
        HealthType.entries.associateBy { type ->
            DemoRow(
                "observer.changes.${type.identifier}",
                "Changes of ${type.identifier} since last tap",
                "observer.changes",
            )
        }

    override val section = DemoSection("Observer", listOf(observe) + changeRows.keys)

    override fun perform(
        row: DemoRow,
        activity: ComponentActivity,
    ): Flow<String> {
        val type = changeRows[row]
        return if (type == null) observeSteps() else changes(type)
    }

    private fun observeSteps(): Flow<String> =
        flow {
            val observer = reporter().observer
            var count = 0
            observer
                .observe(observer.anchor(setOf(HealthType.STEPS)), 10.seconds)
                .map { "Change set #${++count}\n${it.summary}" }
                .onStart { emit("Waiting for steps changes… insert some in the Writer section") }
                .collect { emit(it) }
        }

    private fun changes(type: HealthType): Flow<String> =
        oneShot {
            val observer = reporter().observer
            val anchor = anchors[type]
            if (anchor == null) {
                anchors[type] = observer.anchor(setOf(type))
                "Anchor created; change ${type.identifier} data and tap again"
            } else {
                val changeSet = observer.changes(anchor)
                anchors[type] = changeSet.anchor
                changeSet.summary
            }
        }

    private val ChangeSet.summary: String
        get() = "${upserted.size} upserted, ${deletedIds.size} deleted\n" + upserted.map { it.json }.prettyList()
}
