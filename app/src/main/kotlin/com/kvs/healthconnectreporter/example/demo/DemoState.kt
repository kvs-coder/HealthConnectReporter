package com.kvs.healthconnectreporter.example.demo

import androidx.activity.ComponentActivity
import com.kvs.healthconnectreporter.model.Availability

/** Everything the demo screen shows. */
data class DemoState(
    val availability: Availability,
    val sections: List<DemoSection>,
    val results: Map<String, RowResult> = emptyMap(),
)

/** The state of one demo row. */
sealed interface RowResult {
    /** The demo is running; [output] holds the latest result of a long-running demo. */
    data class Running(
        val output: String? = null,
    ) : RowResult

    data class Success(
        val output: String,
    ) : RowResult

    data class Failure(
        val message: String,
    ) : RowResult
}

/** User intents. */
sealed interface DemoEvent {
    /** Runs the demo, or stops it when it is still running. */
    data class RowTapped(
        val row: DemoRow,
        val activity: ComponentActivity,
    ) : DemoEvent

    /** Runs every non-interactive demo one after another and logs each result. */
    data class RunAllTapped(
        val activity: ComponentActivity,
    ) : DemoEvent

    /** Clears every result. */
    data object ClearTapped : DemoEvent
}
