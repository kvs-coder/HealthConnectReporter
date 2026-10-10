package com.kvs.healthconnectreporter.example.service

import android.content.Context
import androidx.activity.ComponentActivity
import com.kvs.healthconnectreporter.HealthConnectReporter
import com.kvs.healthconnectreporter.example.demo.DemoRow
import com.kvs.healthconnectreporter.example.demo.DemoSection
import com.kvs.healthconnectreporter.model.Availability
import kotlinx.coroutines.flow.Flow

/** Owns one **HealthConnectReporter** and one performer per library area. */
class HealthConnectReporterService(
    context: Context,
) {
    private val appContext = context.applicationContext

    // Created on first use: connecting throws HealthConnectException.NotAvailable without Health Connect.
    private val reporter by lazy { HealthConnectReporter(appContext) }
    private val performers: List<DemoPerformer> =
        listOf(
            ManagerDemos(appContext) { reporter },
            ReaderDemos(appContext) { reporter },
            WriterDemos { reporter },
            ObserverDemos { reporter },
        )

    val availability: Availability get() = HealthConnectReporter.availability(appContext)

    val sections: List<DemoSection> get() = performers.map { it.section }

    /**
     * Every non-interactive row, ordered so each produces a meaningful result: anchors before writes, reads and
     * changes after them, deletions (which clean up the demo data) last.
     */
    val runAllRows: List<DemoRow>
        get() {
            val rows = sections.flatMap { it.rows }.filterNot { it.isInteractive }
            val (manager, rest) = rows.partition { it.id.startsWith("manager.") }
            val changes = rest.filter { it.id.startsWith("observer.changes.") }
            val deletions = rest.filter { it.id.startsWith("writer.delete") }.sortedBy { it.id == "writer.deleteToday" }
            val writes = rest.filter { it.id.startsWith("writer.") } - deletions.toSet()
            val reads = rest.filter { it.id.startsWith("reader.") }
            return manager + changes + writes + reads + changes + deletions + changes
        }

    fun publisher(
        row: DemoRow,
        activity: ComponentActivity,
    ): Flow<String> = performers.first { performer -> row in performer.section.rows }.perform(row, activity)
}
