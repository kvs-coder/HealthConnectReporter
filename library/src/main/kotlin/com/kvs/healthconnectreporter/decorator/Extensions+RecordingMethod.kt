package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.metadata.Metadata
import com.kvs.healthconnectreporter.model.type.RecordingMethod

private val recordingMethodConstants =
    ConstantFamily(
        RecordingMethod.UNKNOWN,
        Metadata.RECORDING_METHOD_UNKNOWN to RecordingMethod.UNKNOWN,
        Metadata.RECORDING_METHOD_ACTIVELY_RECORDED to RecordingMethod.ACTIVELY_RECORDED,
        Metadata.RECORDING_METHOD_AUTOMATICALLY_RECORDED to RecordingMethod.AUTOMATICALLY_RECORDED,
        Metadata.RECORDING_METHOD_MANUAL_ENTRY to RecordingMethod.MANUAL_ENTRY,
    )

internal val RecordingMethod.constant: Int
    get() = recordingMethodConstants.constant(this)

internal val Int.asRecordingMethod: RecordingMethod
    get() = recordingMethodConstants.entry(this)
