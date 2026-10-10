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

    fun publisher(
        row: DemoRow,
        activity: ComponentActivity,
    ): Flow<String> = performers.first { performer -> row in performer.section.rows }.perform(row, activity)
}
