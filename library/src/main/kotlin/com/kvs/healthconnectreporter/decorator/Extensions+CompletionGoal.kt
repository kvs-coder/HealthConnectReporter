package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.ExerciseCompletionGoal
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession.CompletionGoal

internal val CompletionGoal.asOriginal: ExerciseCompletionGoal
    get() =
        when (this) {
            is CompletionGoal.Distance -> ExerciseCompletionGoal.DistanceGoal(distance.asLength)
            is CompletionGoal.DistanceAndDuration ->
                ExerciseCompletionGoal.DistanceAndDurationGoal(distance.asLength, durationMillis.asDuration)
            is CompletionGoal.Steps -> ExerciseCompletionGoal.StepsGoal(steps)
            is CompletionGoal.Duration -> ExerciseCompletionGoal.DurationGoal(durationMillis.asDuration)
            is CompletionGoal.Repetitions -> ExerciseCompletionGoal.RepetitionsGoal(repetitions)
            is CompletionGoal.TotalCaloriesBurned ->
                ExerciseCompletionGoal.TotalCaloriesBurnedGoal(totalCalories.asEnergy)
            is CompletionGoal.ActiveCaloriesBurned ->
                ExerciseCompletionGoal.ActiveCaloriesBurnedGoal(activeCalories.asEnergy)
            CompletionGoal.ManualCompletion -> ExerciseCompletionGoal.ManualCompletion
            CompletionGoal.Unknown -> ExerciseCompletionGoal.UnknownGoal
        }

internal val ExerciseCompletionGoal.harmonized: CompletionGoal
    get() =
        when (this) {
            is ExerciseCompletionGoal.DistanceGoal -> CompletionGoal.Distance(distance.meters)
            is ExerciseCompletionGoal.DistanceAndDurationGoal ->
                CompletionGoal.DistanceAndDuration(distance.meters, duration.millis)
            is ExerciseCompletionGoal.StepsGoal -> CompletionGoal.Steps(steps)
            is ExerciseCompletionGoal.DurationGoal -> CompletionGoal.Duration(duration.millis)
            is ExerciseCompletionGoal.RepetitionsGoal -> CompletionGoal.Repetitions(repetitions)
            is ExerciseCompletionGoal.TotalCaloriesBurnedGoal ->
                CompletionGoal.TotalCaloriesBurned(totalCalories.kilocalories)
            is ExerciseCompletionGoal.ActiveCaloriesBurnedGoal ->
                CompletionGoal.ActiveCaloriesBurned(activeCalories.kilocalories)
            ExerciseCompletionGoal.ManualCompletion -> CompletionGoal.ManualCompletion
            // UnknownGoal and goals added to Health Connect after this library version
            else -> CompletionGoal.Unknown
        }
