package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **SleepStageType** the stage of a sleep session.
 *
 * Mirrors Health Connect's `SleepSessionRecord.STAGE_TYPE_*` constants entry for entry.
 */
@Serializable
public enum class SleepStageType {
    UNKNOWN,
    AWAKE,
    SLEEPING,
    OUT_OF_BED,
    LIGHT,
    DEEP,
    REM,
    AWAKE_IN_BED,
}
