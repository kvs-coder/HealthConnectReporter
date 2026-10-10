package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.ExercisePerformanceTarget
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession.PerformanceTarget

internal val PerformanceTarget.asOriginal: ExercisePerformanceTarget
    get() =
        when (this) {
            is PerformanceTarget.Power -> ExercisePerformanceTarget.PowerTarget(minPower.asPower, maxPower.asPower)
            is PerformanceTarget.Speed ->
                ExercisePerformanceTarget.SpeedTarget(minSpeed.asVelocity, maxSpeed.asVelocity)
            is PerformanceTarget.Cadence -> ExercisePerformanceTarget.CadenceTarget(minCadence, maxCadence)
            is PerformanceTarget.HeartRate -> ExercisePerformanceTarget.HeartRateTarget(minHeartRate, maxHeartRate)
            is PerformanceTarget.Weight -> ExercisePerformanceTarget.WeightTarget(mass.asMass)
            is PerformanceTarget.RateOfPerceivedExertion -> ExercisePerformanceTarget.RateOfPerceivedExertionTarget(rpe)
            PerformanceTarget.Amrap -> ExercisePerformanceTarget.AmrapTarget
            PerformanceTarget.Unknown -> ExercisePerformanceTarget.UnknownTarget
        }

internal val ExercisePerformanceTarget.harmonized: PerformanceTarget
    get() =
        when (this) {
            is ExercisePerformanceTarget.PowerTarget -> PerformanceTarget.Power(minPower.watts, maxPower.watts)
            is ExercisePerformanceTarget.SpeedTarget ->
                PerformanceTarget.Speed(minSpeed.metersPerSecond, maxSpeed.metersPerSecond)
            is ExercisePerformanceTarget.CadenceTarget -> PerformanceTarget.Cadence(minCadence, maxCadence)
            is ExercisePerformanceTarget.HeartRateTarget -> PerformanceTarget.HeartRate(minHeartRate, maxHeartRate)
            is ExercisePerformanceTarget.WeightTarget -> PerformanceTarget.Weight(mass.kilograms)
            is ExercisePerformanceTarget.RateOfPerceivedExertionTarget -> PerformanceTarget.RateOfPerceivedExertion(rpe)
            ExercisePerformanceTarget.AmrapTarget -> PerformanceTarget.Amrap
            // UnknownTarget and targets added to Health Connect after this library version
            else -> PerformanceTarget.Unknown
        }
