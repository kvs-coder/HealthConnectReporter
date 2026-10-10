package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **Feature** a Health Connect feature that may be missing on older Health Connect versions.
 *
 * Mirrors Health Connect's `HealthConnectFeatures.FEATURE_*` constants, without personal health records.
 */
@Serializable
public enum class Feature {
    /** reading health data in the background, **Permission.ReadInBackground** */
    READ_HEALTH_DATA_IN_BACKGROUND,

    /** skin temperature, [HealthType.SKIN_TEMPERATURE] */
    SKIN_TEMPERATURE,

    /** planned exercise sessions, [HealthType.PLANNED_EXERCISE_SESSION] */
    PLANNED_EXERCISE,

    /** reading data older than 30 days before the first grant, **Permission.ReadHistory** */
    READ_HEALTH_DATA_HISTORY,

    /** mindfulness sessions, [HealthType.MINDFULNESS_SESSION] */
    MINDFULNESS_SESSION,
}
