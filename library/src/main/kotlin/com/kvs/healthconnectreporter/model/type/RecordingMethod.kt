package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **RecordingMethod** how a record was recorded.
 *
 * Mirrors Health Connect's `Metadata.RECORDING_METHOD_*` constants entry for entry.
 */
@Serializable
public enum class RecordingMethod {
    UNKNOWN,
    ACTIVELY_RECORDED,
    AUTOMATICALLY_RECORDED,
    MANUAL_ENTRY,
}
