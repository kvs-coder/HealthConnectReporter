package com.kvs.healthconnectreporter.example.service

import androidx.activity.ComponentActivity
import com.kvs.healthconnectreporter.HealthConnectReporter
import com.kvs.healthconnectreporter.example.demo.DemoRow
import com.kvs.healthconnectreporter.example.demo.DemoSection
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.payload.Steps
import com.kvs.healthconnectreporter.model.payload.WritableSample
import com.kvs.healthconnectreporter.model.type.HealthType
import kotlinx.coroutines.flow.Flow

/** Demos of **HealthConnectWriter**: insert one sample of every type, then update and delete them. */
class WriterDemos(
    private val reporter: () -> HealthConnectReporter,
) : DemoPerformer {
    private val inserted = mutableMapOf<HealthType, WritableSample>()
    private val writer get() = reporter().writer

    private val demos =
        linkedMapOf<DemoRow, suspend () -> String>(
            DemoRow("writer.insertAll", "Insert one sample of every available type", "writer.insert(list)") to {
                val manager = reporter().manager
                val types =
                    HealthType.entries.filter { type ->
                        type.feature?.let { manager.isFeatureAvailable(it) }
                            ?: true
                    }
                val samples = writer.insert(types.map { it.demoSample(System.currentTimeMillis()) })
                samples.forEach { inserted[it.healthType] = it }
                "Inserted ${samples.size} samples:\n" + samples.map { it.json }.prettyList()
            },
            DemoRow("writer.update", "Add 100 to the inserted steps", "writer.update") to {
                val steps = inserted[HealthType.STEPS] as? Steps
                if (steps == null) {
                    "Insert steps first"
                } else {
                    val updated =
                        steps.copy(
                            harmonized = Steps.Harmonized(steps.harmonized.count + 100),
                            clientRecordVersion = (steps.clientRecordVersion ?: 0) + 1,
                        )
                    writer.update(updated)
                    inserted[HealthType.STEPS] = updated
                    updated.json.pretty()
                }
            },
            DemoRow("writer.deleteById", "Delete the inserted heart rate by id", "writer.delete(type, ids)") to {
                val id = inserted.remove(HealthType.HEART_RATE)?.id
                if (id == null) {
                    "Insert heart rate first"
                } else {
                    writer.delete(HealthType.HEART_RATE, ids = listOf(id))
                    "Deleted $id"
                }
            },
            DemoRow("writer.deleteRange", "Delete today's steps written by this app", "writer.delete(type, range)") to {
                writer.delete(HealthType.STEPS, TimeRange.day())
                inserted.remove(HealthType.STEPS)
                "Deleted"
            },
            DemoRow("writer.deleteInserted", "Delete every inserted sample", "writer.delete(sample)") to {
                val samples = inserted.values.toList()
                samples.forEach { writer.delete(it) }
                inserted.clear()
                "Deleted ${samples.size} samples"
            },
        ).apply {
            HealthType.entries.forEach { type ->
                put(DemoRow("writer.insert.${type.identifier}", "Insert one ${type.identifier}", "writer.insert")) {
                    val sample = writer.insert(type.demoSample(System.currentTimeMillis()))
                    inserted[type] = sample
                    sample.json.pretty()
                }
            }
        }

    override val section = DemoSection("Writer", demos.keys.toList())

    override fun perform(
        row: DemoRow,
        activity: ComponentActivity,
    ): Flow<String> = oneShot { demos.getValue(row)() }
}
