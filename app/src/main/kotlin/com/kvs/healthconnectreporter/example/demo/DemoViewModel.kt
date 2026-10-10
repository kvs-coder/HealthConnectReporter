package com.kvs.healthconnectreporter.example.demo

import android.app.Application
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kvs.healthconnectreporter.example.service.HealthConnectReporterService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Turns [DemoEvent]s into demo runs and exposes their results as [DemoState]. */
class DemoViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val service = HealthConnectReporterService(application)
    private val jobs = mutableMapOf<String, Job>()
    private val mutableState =
        MutableStateFlow(
            DemoState(
                availability = service.availability,
                sections = service.sections,
            ),
        )

    val state: StateFlow<DemoState> = mutableState.asStateFlow()

    fun onEvent(event: DemoEvent) {
        when (event) {
            is DemoEvent.RowTapped -> toggle(event.row, event.activity)
            is DemoEvent.RunAllTapped -> runAll(event.activity)
            DemoEvent.ClearTapped -> {
                jobs.values.forEach { it.cancel() }
                jobs.clear()
                mutableState.update { it.copy(results = emptyMap()) }
            }
        }
    }

    private fun toggle(
        row: DemoRow,
        activity: ComponentActivity,
    ) {
        jobs.remove(row.id)?.let { running ->
            running.cancel()
            setResult(row, RowResult.Success("Stopped"))
            return
        }
        setResult(row, RowResult.Running())
        jobs[row.id] =
            viewModelScope.launch {
                var last: String? = null
                service
                    .publisher(row, activity)
                    .onCompletion { error ->
                        jobs.remove(row.id)
                        if (error == null) setResult(row, RowResult.Success(last ?: "Done"))
                    }.catch { error ->
                        setResult(row, RowResult.Failure("${error::class.simpleName}: ${error.message}"))
                    }.collect { output ->
                        last = output
                        setResult(row, RowResult.Running(output))
                    }
            }
    }

    private fun runAll(activity: ComponentActivity) {
        if (jobs.containsKey(RUN_ALL)) return
        jobs[RUN_ALL] =
            viewModelScope.launch {
                val rows = service.runAllRows
                var failures = 0
                rows.forEachIndexed { index, row ->
                    setResult(row, RowResult.Running())
                    val result =
                        service
                            .publisher(row, activity)
                            .map<String, RowResult> { RowResult.Success(it) }
                            .catch { error -> emit(RowResult.Failure("${error::class.simpleName}: ${error.message}")) }
                            .last()
                    if (result is RowResult.Failure) failures++
                    setResult(row, result)
                    Log.i(TAG, "RESULT ${index + 1}/${rows.size} ${row.id} ${result.summary}")
                }
                Log.i(TAG, "RUN ALL DONE ${rows.size} rows, $failures failed")
                jobs.remove(RUN_ALL)
            }
    }

    private val RowResult.summary: String
        get() =
            when (this) {
                is RowResult.Running -> "RUNNING"
                is RowResult.Success -> "SUCCESS ${output.lineSequence().first()}"
                is RowResult.Failure -> "FAILURE $message"
            }

    private fun setResult(
        row: DemoRow,
        result: RowResult,
    ) {
        mutableState.update { it.copy(results = it.results + (row.id to result)) }
    }

    private companion object {
        const val TAG = "HealthConnectDemo"
        const val RUN_ALL = "run-all"
    }
}
