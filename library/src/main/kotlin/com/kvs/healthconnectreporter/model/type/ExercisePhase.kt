package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **ExercisePhase** the phase of a planned exercise step.
 *
 * Mirrors Health Connect's `PlannedExerciseStep.EXERCISE_PHASE_*` constants entry for entry.
 */
@Serializable
public enum class ExercisePhase {
    UNKNOWN,
    WARMUP,
    REST,
    ACTIVE,
    COOLDOWN,
    RECOVERY,
}
