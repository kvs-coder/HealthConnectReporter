package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **BodyPosition** the body position during a blood pressure measurement.
 *
 * Mirrors Health Connect's `BloodPressureRecord.BODY_POSITION_*` constants entry for entry.
 */
@Serializable
public enum class BodyPosition {
    UNKNOWN,
    STANDING_UP,
    SITTING_DOWN,
    LYING_DOWN,
    RECLINING,
}
