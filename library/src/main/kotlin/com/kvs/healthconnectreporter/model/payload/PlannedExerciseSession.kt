package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.PlannedExerciseBlock
import androidx.health.connect.client.records.PlannedExerciseSessionRecord
import androidx.health.connect.client.records.PlannedExerciseStep
import com.kvs.healthconnectreporter.decorator.asDuration
import com.kvs.healthconnectreporter.decorator.asExercisePhase
import com.kvs.healthconnectreporter.decorator.asExerciseSegmentType
import com.kvs.healthconnectreporter.decorator.asExerciseType
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asZoneOffset
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.decorator.encoded
import com.kvs.healthconnectreporter.decorator.epochMillis
import com.kvs.healthconnectreporter.decorator.harmonized
import com.kvs.healthconnectreporter.decorator.lastModifiedTimestamp
import com.kvs.healthconnectreporter.decorator.metadata
import com.kvs.healthconnectreporter.decorator.sampleClientRecordVersion
import com.kvs.healthconnectreporter.decorator.sampleDataOrigin
import com.kvs.healthconnectreporter.decorator.sampleDevice
import com.kvs.healthconnectreporter.decorator.sampleId
import com.kvs.healthconnectreporter.decorator.sampleRecordingMethod
import com.kvs.healthconnectreporter.decorator.seconds
import com.kvs.healthconnectreporter.decorator.validateInterval
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Payload
import com.kvs.healthconnectreporter.model.type.ExercisePhase
import com.kvs.healthconnectreporter.model.type.ExerciseSegmentType
import com.kvs.healthconnectreporter.model.type.ExerciseType
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZoneOffset

/**
 * **PlannedExerciseSession** a planned exercise session (training plan) of blocks of steps.
 *
 * @param startTimestamp **Long** epoch milliseconds
 * @param endTimestamp **Long** epoch milliseconds, not before [startTimestamp]
 * @param harmonized **Harmonized** the measured values
 * @param id **String?** Health Connect record id; null until inserted
 * @param clientRecordId **String?** your own id for the record
 * @param clientRecordVersion **Long?** your own version of [clientRecordId]
 * @param startZoneOffsetSeconds **Int?** zone offset at the start in seconds
 * @param endZoneOffsetSeconds **Int?** zone offset at the end in seconds
 * @param recordingMethod **RecordingMethod?** how the data was recorded; manual entry when null
 * @param device **Device?** the recording device
 * @param dataOrigin **DataOrigin?** the writing app; set by Health Connect
 * @param lastModifiedTimestamp **Long?** last modification, epoch milliseconds; set by Health Connect
 * @throws com.kvs.healthconnectreporter.HealthConnectException.InvalidValue when [endTimestamp] is before
 * [startTimestamp]
 */
@Serializable
@SerialName("planned_exercise")
public data class PlannedExerciseSession(
    override val startTimestamp: Long,
    override val endTimestamp: Long,
    val harmonized: Harmonized,
    override val id: String? = null,
    override val clientRecordId: String? = null,
    override val clientRecordVersion: Long? = null,
    override val startZoneOffsetSeconds: Int? = null,
    override val endZoneOffsetSeconds: Int? = null,
    override val recordingMethod: RecordingMethod? = null,
    override val device: Device? = null,
    override val dataOrigin: DataOrigin? = null,
    override val lastModifiedTimestamp: Long? = null,
) : WritableSample {
    init {
        validateInterval()
    }

    override val healthType: HealthType get() = HealthType.PLANNED_EXERCISE_SESSION

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of PlannedExerciseSession.
     *
     * @param exerciseType **ExerciseType** the type of exercise
     * @param blocks **List<Block>** the blocks of the plan, in order
     * @param title **String?** the title of the plan
     * @param notes **String?** notes on the plan
     * @param hasExplicitTime **Boolean** the plan has a start and end time; when false it is planned for the day of
     * the start (stored by Health Connect at noon, lasting until the end). True by default
     * @param completedExerciseSessionId **String?** id of the **ExerciseSession** that completed the plan;
     * set by Health Connect
     */
    @Serializable
    public data class Harmonized(
        val exerciseType: ExerciseType,
        val blocks: List<Block>,
        val title: String? = null,
        val notes: String? = null,
        val hasExplicitTime: Boolean = true,
        val completedExerciseSessionId: String? = null,
    )

    /**
     * **Block** steps repeated a number of times.
     *
     * @param repetitions **Int** how often the steps are repeated, at least 1
     * @param steps **List<Step>** the steps, in order
     * @param description **String?** a description of the block
     */
    @Serializable
    public data class Block(
        val repetitions: Int,
        val steps: List<Step>,
        val description: String? = null,
    )

    /**
     * **Step** one planned step, e.g. a warm-up or an interval.
     *
     * @param exerciseType **ExerciseSegmentType** the exercise of the step
     * @param exercisePhase **ExercisePhase** the phase of the step
     * @param completionGoal **CompletionGoal** when the step is complete
     * @param performanceTargets **List<PerformanceTarget>** targets to hold during the step. Empty by default
     * @param description **String?** a description of the step
     */
    @Serializable
    public data class Step(
        val exerciseType: ExerciseSegmentType,
        val exercisePhase: ExercisePhase,
        val completionGoal: CompletionGoal,
        val performanceTargets: List<PerformanceTarget> = emptyList(),
        val description: String? = null,
    )

    /** **CompletionGoal** when a planned step is complete; JSON carries a `type` discriminator */
    @Serializable
    public sealed interface CompletionGoal {
        /**
         * **Distance** complete after a distance.
         *
         * @param distance **Double** distance in m
         */
        @Serializable
        @SerialName("distance")
        public data class Distance(
            val distance: Double,
        ) : CompletionGoal

        /**
         * **DistanceAndDuration** complete after a distance, in a duration.
         *
         * @param distance **Double** distance in m
         * @param durationMillis **Long** duration in milliseconds
         */
        @Serializable
        @SerialName("distance_and_duration")
        public data class DistanceAndDuration(
            val distance: Double,
            val durationMillis: Long,
        ) : CompletionGoal

        /**
         * **Steps** complete after a number of steps.
         *
         * @param steps **Int** number of steps
         */
        @Serializable
        @SerialName("steps")
        public data class Steps(
            val steps: Int,
        ) : CompletionGoal

        /**
         * **Duration** complete after a duration.
         *
         * @param durationMillis **Long** duration in milliseconds
         */
        @Serializable
        @SerialName("duration")
        public data class Duration(
            val durationMillis: Long,
        ) : CompletionGoal

        /**
         * **Repetitions** complete after a number of repetitions.
         *
         * @param repetitions **Int** number of repetitions
         */
        @Serializable
        @SerialName("repetitions")
        public data class Repetitions(
            val repetitions: Int,
        ) : CompletionGoal

        /**
         * **TotalCaloriesBurned** complete after burning calories.
         *
         * @param totalCalories **Double** total calories in kcal
         */
        @Serializable
        @SerialName("total_calories_burned")
        public data class TotalCaloriesBurned(
            val totalCalories: Double,
        ) : CompletionGoal

        /**
         * **ActiveCaloriesBurned** complete after burning active calories.
         *
         * @param activeCalories **Double** active calories in kcal
         */
        @Serializable
        @SerialName("active_calories_burned")
        public data class ActiveCaloriesBurned(
            val activeCalories: Double,
        ) : CompletionGoal

        /** **ManualCompletion** the user completes the step */
        @Serializable
        @SerialName("manual_completion")
        public data object ManualCompletion : CompletionGoal

        /** **Unknown** a goal this library version does not know */
        @Serializable
        @SerialName("unknown")
        public data object Unknown : CompletionGoal
    }

    /** **PerformanceTarget** a target to hold during a planned step; JSON carries a `type` discriminator */
    @Serializable
    public sealed interface PerformanceTarget {
        /**
         * **Power** a power range.
         *
         * @param minPower **Double** minimum power in W
         * @param maxPower **Double** maximum power in W
         */
        @Serializable
        @SerialName("power")
        public data class Power(
            val minPower: Double,
            val maxPower: Double,
        ) : PerformanceTarget

        /**
         * **Speed** a speed range.
         *
         * @param minSpeed **Double** minimum speed in m/s
         * @param maxSpeed **Double** maximum speed in m/s
         */
        @Serializable
        @SerialName("speed")
        public data class Speed(
            val minSpeed: Double,
            val maxSpeed: Double,
        ) : PerformanceTarget

        /**
         * **Cadence** a cadence range.
         *
         * @param minCadence **Double** minimum cadence in steps or revolutions per minute
         * @param maxCadence **Double** maximum cadence in steps or revolutions per minute
         */
        @Serializable
        @SerialName("cadence")
        public data class Cadence(
            val minCadence: Double,
            val maxCadence: Double,
        ) : PerformanceTarget

        /**
         * **HeartRate** a heart rate range.
         *
         * @param minHeartRate **Double** minimum heart rate in beats per minute
         * @param maxHeartRate **Double** maximum heart rate in beats per minute
         */
        @Serializable
        @SerialName("heart_rate")
        public data class HeartRate(
            val minHeartRate: Double,
            val maxHeartRate: Double,
        ) : PerformanceTarget

        /**
         * **Weight** a weight to lift.
         *
         * @param mass **Double** weight in kg
         */
        @Serializable
        @SerialName("weight")
        public data class Weight(
            val mass: Double,
        ) : PerformanceTarget

        /**
         * **RateOfPerceivedExertion** a perceived exertion.
         *
         * @param rpe **Int** rate of perceived exertion (0–10)
         */
        @Serializable
        @SerialName("rate_of_perceived_exertion")
        public data class RateOfPerceivedExertion(
            val rpe: Int,
        ) : PerformanceTarget

        /** **Amrap** as many repetitions as possible */
        @Serializable
        @SerialName("amrap")
        public data object Amrap : PerformanceTarget

        /** **Unknown** a target this library version does not know */
        @Serializable
        @SerialName("unknown")
        public data object Unknown : PerformanceTarget
    }

    // region Original
    internal fun asOriginal(): PlannedExerciseSessionRecord {
        val blocks =
            harmonized.blocks.map { block ->
                PlannedExerciseBlock(
                    repetitions = block.repetitions,
                    steps =
                        block.steps.map {
                            PlannedExerciseStep(
                                exerciseType = it.exerciseType.constant,
                                exercisePhase = it.exercisePhase.constant,
                                completionGoal = it.completionGoal.asOriginal,
                                performanceTargets = it.performanceTargets.map { target -> target.asOriginal },
                                description = it.description,
                            )
                        },
                    description = block.description,
                )
            }
        if (!harmonized.hasExplicitTime) {
            val offset = startZoneOffsetSeconds.asZoneOffset ?: ZoneOffset.UTC
            return PlannedExerciseSessionRecord(
                metadata = metadata(),
                startDate = startTimestamp.asInstant.atOffset(offset).toLocalDate(),
                duration = (endTimestamp - startTimestamp).asDuration,
                blocks = blocks,
                exerciseType = harmonized.exerciseType.constant,
                title = harmonized.title,
                notes = harmonized.notes,
            )
        }
        return PlannedExerciseSessionRecord(
            startTime = startTimestamp.asInstant,
            startZoneOffset = startZoneOffsetSeconds.asZoneOffset,
            endTime = endTimestamp.asInstant,
            endZoneOffset = endZoneOffsetSeconds.asZoneOffset,
            metadata = metadata(),
            blocks = blocks,
            exerciseType = harmonized.exerciseType.constant,
            title = harmonized.title,
            notes = harmonized.notes,
        )
    }
    // endregion

    /** Factory of **PlannedExerciseSession** */
    public companion object : Payload.Factory<PlannedExerciseSession>({ PlannedExerciseSession.serializer() }) {
        internal fun from(record: PlannedExerciseSessionRecord): PlannedExerciseSession =
            PlannedExerciseSession(
                startTimestamp = record.startTime.epochMillis,
                endTimestamp = record.endTime.epochMillis,
                harmonized =
                    Harmonized(
                        exerciseType = record.exerciseType.asExerciseType,
                        blocks =
                            record.blocks.map { block ->
                                Block(
                                    repetitions = block.repetitions,
                                    steps =
                                        block.steps.map {
                                            Step(
                                                exerciseType = it.exerciseType.asExerciseSegmentType,
                                                exercisePhase = it.exercisePhase.asExercisePhase,
                                                completionGoal = it.completionGoal.harmonized,
                                                performanceTargets =
                                                    it.performanceTargets.map { target -> target.harmonized },
                                                description = it.description,
                                            )
                                        },
                                    description = block.description,
                                )
                            },
                        title = record.title,
                        notes = record.notes,
                        hasExplicitTime = record.hasExplicitTime,
                        completedExerciseSessionId = record.completedExerciseSessionId,
                    ),
                id = record.metadata.sampleId,
                clientRecordId = record.metadata.clientRecordId,
                clientRecordVersion = record.metadata.sampleClientRecordVersion,
                startZoneOffsetSeconds = record.startZoneOffset.seconds,
                endZoneOffsetSeconds = record.endZoneOffset.seconds,
                recordingMethod = record.metadata.sampleRecordingMethod,
                device = record.metadata.sampleDevice,
                dataOrigin = record.metadata.sampleDataOrigin,
                lastModifiedTimestamp = record.metadata.lastModifiedTimestamp,
            )
    }
}
