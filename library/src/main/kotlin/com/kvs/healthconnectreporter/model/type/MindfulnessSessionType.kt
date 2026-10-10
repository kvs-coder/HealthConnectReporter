package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **MindfulnessSessionType** the type of a mindfulness session.
 *
 * Mirrors Health Connect's `MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_*` constants entry for entry.
 */
@Serializable
public enum class MindfulnessSessionType {
    UNKNOWN,
    MEDITATION,
    BREATHING,
    MUSIC,
    MOVEMENT,
    UNGUIDED,
}
