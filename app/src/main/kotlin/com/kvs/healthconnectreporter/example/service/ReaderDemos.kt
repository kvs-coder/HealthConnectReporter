package com.kvs.healthconnectreporter.example.service

import android.content.Context
import androidx.activity.ComponentActivity
import com.kvs.healthconnectreporter.HealthConnectReporter
import com.kvs.healthconnectreporter.example.demo.DemoRow
import com.kvs.healthconnectreporter.example.demo.DemoSection
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.TimeGroup
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.type.Aggregation
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.TimeGroupUnit
import com.kvs.healthconnectreporter.service.aggregate
import kotlinx.coroutines.flow.Flow

/** Demos of **HealthConnectReader**: every type, every aggregation, paging, read by id and filters. */
class ReaderDemos(
    private val context: Context,
    private val reporter: () -> HealthConnectReporter,
) : DemoPerformer {
    private val reader get() = reporter().reader
    private val week get() = TimeRange.lastDays(7)

    private val demos =
        linkedMapOf<DemoRow, suspend () -> String>(
            DemoRow("reader.page", "Steps, two pages of 5", "reader.readPage") to {
                val first = reader.readPage(HealthType.STEPS, week, pageSize = 5)
                val second = first.nextPageToken?.let { reader.readPage(HealthType.STEPS, week, 5, it) }
                "Page 1 (${first.items.size}):\n${first.items.map { it.json }.prettyList()}\n\n" +
                    "Page 2 (${second?.items?.size ?: 0}):\n${second?.items.orEmpty().map { it.json }.prettyList()}"
            },
            DemoRow("reader.byId", "Latest steps record, read again by id", "reader.read(type, id)") to {
                val latest = reader.read(HealthType.STEPS, week, limit = 1).firstOrNull()
                val id = latest?.id
                if (id == null) "No steps in the last 7 days" else reader.read(HealthType.STEPS, id).json.pretty()
            },
            DemoRow("reader.origins", "Steps written by this app", "reader.read(origins)") to {
                reader
                    .read(HealthType.STEPS, week, limit = 20, origins = setOf(DataOrigin(context.packageName)))
                    .map { it.json }
                    .prettyList()
            },
            DemoRow("reader.hourly", "Steps per hour today", "reader.aggregate + TimeGroup.Duration") to {
                reader
                    .aggregate(setOf(Aggregation.STEPS_COUNT_TOTAL), TimeRange.day(), TimeGroup.Duration(HOUR))
                    .filter { it.value != null }
                    .map { it.json }
                    .prettyList()
            },
        ).apply {
            HealthType.entries.forEach { type ->
                put(DemoRow("reader.read.${type.identifier}", "Read ${type.identifier}, last 7 days", "reader.read")) {
                    reader.read(type, week, limit = 20).map { it.json }.prettyList()
                }
            }
            Aggregation.entries.forEach { aggregation ->
                val row =
                    DemoRow(
                        "reader.aggregate.${aggregation.name}",
                        "${aggregation.name.lowercase()} per day, last 7 days",
                        "reader.aggregate + TimeGroup.Period",
                    )
                put(row) {
                    reader
                        .aggregate(setOf(aggregation), week, TimeGroup.Period(TimeGroupUnit.DAILY))
                        .map { it.json }
                        .prettyList()
                }
            }
        }

    override val section = DemoSection("Reader", demos.keys.toList())

    override fun perform(
        row: DemoRow,
        activity: ComponentActivity,
    ): Flow<String> = oneShot { demos.getValue(row)() }

    private companion object {
        const val HOUR = 3_600_000L
    }
}
