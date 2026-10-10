package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.ExerciseSegment
import com.kvs.healthconnectreporter.model.type.ExerciseSegmentType

private val exerciseSegmentTypeConstants =
    ConstantFamily(
        ExerciseSegmentType.UNKNOWN,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_UNKNOWN to ExerciseSegmentType.UNKNOWN,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_ARM_CURL to ExerciseSegmentType.ARM_CURL,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_BACK_EXTENSION to ExerciseSegmentType.BACK_EXTENSION,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_BALL_SLAM to ExerciseSegmentType.BALL_SLAM,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_BARBELL_SHOULDER_PRESS to ExerciseSegmentType.BARBELL_SHOULDER_PRESS,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_BENCH_PRESS to ExerciseSegmentType.BENCH_PRESS,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_BENCH_SIT_UP to ExerciseSegmentType.BENCH_SIT_UP,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_BIKING to ExerciseSegmentType.BIKING,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_BIKING_STATIONARY to ExerciseSegmentType.BIKING_STATIONARY,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_BURPEE to ExerciseSegmentType.BURPEE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_CRUNCH to ExerciseSegmentType.CRUNCH,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DEADLIFT to ExerciseSegmentType.DEADLIFT,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DOUBLE_ARM_TRICEPS_EXTENSION to
            ExerciseSegmentType.DOUBLE_ARM_TRICEPS_EXTENSION,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_CURL_LEFT_ARM to ExerciseSegmentType.DUMBBELL_CURL_LEFT_ARM,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_CURL_RIGHT_ARM to ExerciseSegmentType.DUMBBELL_CURL_RIGHT_ARM,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_FRONT_RAISE to ExerciseSegmentType.DUMBBELL_FRONT_RAISE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_LATERAL_RAISE to ExerciseSegmentType.DUMBBELL_LATERAL_RAISE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_ROW to ExerciseSegmentType.DUMBBELL_ROW,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_LEFT_ARM to
            ExerciseSegmentType.DUMBBELL_TRICEPS_EXTENSION_LEFT_ARM,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_RIGHT_ARM to
            ExerciseSegmentType.DUMBBELL_TRICEPS_EXTENSION_RIGHT_ARM,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_DUMBBELL_TRICEPS_EXTENSION_TWO_ARM to
            ExerciseSegmentType.DUMBBELL_TRICEPS_EXTENSION_TWO_ARM,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_ELLIPTICAL to ExerciseSegmentType.ELLIPTICAL,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_FORWARD_TWIST to ExerciseSegmentType.FORWARD_TWIST,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_FRONT_RAISE to ExerciseSegmentType.FRONT_RAISE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING to
            ExerciseSegmentType.HIGH_INTENSITY_INTERVAL_TRAINING,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_HIP_THRUST to ExerciseSegmentType.HIP_THRUST,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_HULA_HOOP to ExerciseSegmentType.HULA_HOOP,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_JUMPING_JACK to ExerciseSegmentType.JUMPING_JACK,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_JUMP_ROPE to ExerciseSegmentType.JUMP_ROPE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_KETTLEBELL_SWING to ExerciseSegmentType.KETTLEBELL_SWING,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_LATERAL_RAISE to ExerciseSegmentType.LATERAL_RAISE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_LAT_PULL_DOWN to ExerciseSegmentType.LAT_PULL_DOWN,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_CURL to ExerciseSegmentType.LEG_CURL,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_EXTENSION to ExerciseSegmentType.LEG_EXTENSION,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_PRESS to ExerciseSegmentType.LEG_PRESS,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_LEG_RAISE to ExerciseSegmentType.LEG_RAISE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_LUNGE to ExerciseSegmentType.LUNGE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_MOUNTAIN_CLIMBER to ExerciseSegmentType.MOUNTAIN_CLIMBER,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_OTHER_WORKOUT to ExerciseSegmentType.OTHER_WORKOUT,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_PAUSE to ExerciseSegmentType.PAUSE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_PILATES to ExerciseSegmentType.PILATES,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_PLANK to ExerciseSegmentType.PLANK,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_PULL_UP to ExerciseSegmentType.PULL_UP,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_PUNCH to ExerciseSegmentType.PUNCH,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_REST to ExerciseSegmentType.REST,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_ROWING_MACHINE to ExerciseSegmentType.ROWING_MACHINE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_RUNNING to ExerciseSegmentType.RUNNING,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_RUNNING_TREADMILL to ExerciseSegmentType.RUNNING_TREADMILL,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SHOULDER_PRESS to ExerciseSegmentType.SHOULDER_PRESS,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SINGLE_ARM_TRICEPS_EXTENSION to
            ExerciseSegmentType.SINGLE_ARM_TRICEPS_EXTENSION,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SIT_UP to ExerciseSegmentType.SIT_UP,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SQUAT to ExerciseSegmentType.SQUAT,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_STAIR_CLIMBING to ExerciseSegmentType.STAIR_CLIMBING,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_STAIR_CLIMBING_MACHINE to ExerciseSegmentType.STAIR_CLIMBING_MACHINE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_STRETCHING to ExerciseSegmentType.STRETCHING,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BACKSTROKE to ExerciseSegmentType.SWIMMING_BACKSTROKE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BREASTSTROKE to ExerciseSegmentType.SWIMMING_BREASTSTROKE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_BUTTERFLY to ExerciseSegmentType.SWIMMING_BUTTERFLY,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_FREESTYLE to ExerciseSegmentType.SWIMMING_FREESTYLE,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_MIXED to ExerciseSegmentType.SWIMMING_MIXED,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_OPEN_WATER to ExerciseSegmentType.SWIMMING_OPEN_WATER,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_OTHER to ExerciseSegmentType.SWIMMING_OTHER,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_SWIMMING_POOL to ExerciseSegmentType.SWIMMING_POOL,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_UPPER_TWIST to ExerciseSegmentType.UPPER_TWIST,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_WALKING to ExerciseSegmentType.WALKING,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_WEIGHTLIFTING to ExerciseSegmentType.WEIGHTLIFTING,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_WHEELCHAIR to ExerciseSegmentType.WHEELCHAIR,
        ExerciseSegment.EXERCISE_SEGMENT_TYPE_YOGA to ExerciseSegmentType.YOGA,
    )

internal val ExerciseSegmentType.constant: Int
    get() = exerciseSegmentTypeConstants.constant(this)

internal val Int.asExerciseSegmentType: ExerciseSegmentType
    get() = exerciseSegmentTypeConstants.entry(this)
