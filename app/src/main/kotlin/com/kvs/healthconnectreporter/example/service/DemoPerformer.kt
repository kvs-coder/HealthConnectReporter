package com.kvs.healthconnectreporter.example.service

import androidx.activity.ComponentActivity
import com.kvs.healthconnectreporter.example.demo.DemoRow
import com.kvs.healthconnectreporter.example.demo.DemoSection
import kotlinx.coroutines.flow.Flow

/** Runs the demos of one library area. */
interface DemoPerformer {
    val section: DemoSection

    /**
     * Runs one demo of this performer's [section].
     *
     * @return the demo's output; one value for one-shot calls, a stream for observers
     */
    fun perform(
        row: DemoRow,
        activity: ComponentActivity,
    ): Flow<String>
}

/** Pretty JSON for the result views. */
internal fun String.pretty(): String =
    prettyJson.encodeToString(
        kotlinx.serialization.json.JsonElement
            .serializer(),
        prettyJson.parseToJsonElement(this),
    )

internal fun List<String>.prettyList(): String =
    if (isEmpty()) {
        "[] — no data"
    } else {
        joinToString(separator = ",\n", prefix = "[\n", postfix = "\n]") {
            it.pretty()
        }
    }

private val prettyJson = kotlinx.serialization.json.Json { prettyPrint = true }

/** A flow that emits the result of one call. */
internal fun oneShot(call: suspend () -> String): Flow<String> = kotlinx.coroutines.flow.flow { emit(call()) }
