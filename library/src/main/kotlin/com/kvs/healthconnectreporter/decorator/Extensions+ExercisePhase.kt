package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.PlannedExerciseStep
import com.kvs.healthconnectreporter.model.type.ExercisePhase

private val exercisePhaseConstants =
    ConstantFamily(
        ExercisePhase.UNKNOWN,
        PlannedExerciseStep.EXERCISE_PHASE_UNKNOWN to ExercisePhase.UNKNOWN,
        PlannedExerciseStep.EXERCISE_PHASE_WARMUP to ExercisePhase.WARMUP,
        PlannedExerciseStep.EXERCISE_PHASE_REST to ExercisePhase.REST,
        PlannedExerciseStep.EXERCISE_PHASE_ACTIVE to ExercisePhase.ACTIVE,
        PlannedExerciseStep.EXERCISE_PHASE_COOLDOWN to ExercisePhase.COOLDOWN,
        PlannedExerciseStep.EXERCISE_PHASE_RECOVERY to ExercisePhase.RECOVERY,
    )

internal val ExercisePhase.constant: Int
    get() = exercisePhaseConstants.constant(this)

internal val Int.asExercisePhase: ExercisePhase
    get() = exercisePhaseConstants.entry(this)
