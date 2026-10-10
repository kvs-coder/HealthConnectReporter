package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.ExerciseSessionRecord
import com.kvs.healthconnectreporter.model.type.ExerciseType

private val exerciseTypeConstants =
    ConstantFamily(
        ExerciseType.OTHER_WORKOUT,
        ExerciseSessionRecord.EXERCISE_TYPE_OTHER_WORKOUT to ExerciseType.OTHER_WORKOUT,
        ExerciseSessionRecord.EXERCISE_TYPE_BADMINTON to ExerciseType.BADMINTON,
        ExerciseSessionRecord.EXERCISE_TYPE_BASEBALL to ExerciseType.BASEBALL,
        ExerciseSessionRecord.EXERCISE_TYPE_BASKETBALL to ExerciseType.BASKETBALL,
        ExerciseSessionRecord.EXERCISE_TYPE_BIKING to ExerciseType.BIKING,
        ExerciseSessionRecord.EXERCISE_TYPE_BIKING_STATIONARY to ExerciseType.BIKING_STATIONARY,
        ExerciseSessionRecord.EXERCISE_TYPE_BOOT_CAMP to ExerciseType.BOOT_CAMP,
        ExerciseSessionRecord.EXERCISE_TYPE_BOXING to ExerciseType.BOXING,
        ExerciseSessionRecord.EXERCISE_TYPE_CALISTHENICS to ExerciseType.CALISTHENICS,
        ExerciseSessionRecord.EXERCISE_TYPE_CRICKET to ExerciseType.CRICKET,
        ExerciseSessionRecord.EXERCISE_TYPE_DANCING to ExerciseType.DANCING,
        ExerciseSessionRecord.EXERCISE_TYPE_ELLIPTICAL to ExerciseType.ELLIPTICAL,
        ExerciseSessionRecord.EXERCISE_TYPE_EXERCISE_CLASS to ExerciseType.EXERCISE_CLASS,
        ExerciseSessionRecord.EXERCISE_TYPE_FENCING to ExerciseType.FENCING,
        ExerciseSessionRecord.EXERCISE_TYPE_FOOTBALL_AMERICAN to ExerciseType.FOOTBALL_AMERICAN,
        ExerciseSessionRecord.EXERCISE_TYPE_FOOTBALL_AUSTRALIAN to ExerciseType.FOOTBALL_AUSTRALIAN,
        ExerciseSessionRecord.EXERCISE_TYPE_FRISBEE_DISC to ExerciseType.FRISBEE_DISC,
        ExerciseSessionRecord.EXERCISE_TYPE_GOLF to ExerciseType.GOLF,
        ExerciseSessionRecord.EXERCISE_TYPE_GUIDED_BREATHING to ExerciseType.GUIDED_BREATHING,
        ExerciseSessionRecord.EXERCISE_TYPE_GYMNASTICS to ExerciseType.GYMNASTICS,
        ExerciseSessionRecord.EXERCISE_TYPE_HANDBALL to ExerciseType.HANDBALL,
        ExerciseSessionRecord.EXERCISE_TYPE_HIGH_INTENSITY_INTERVAL_TRAINING to
            ExerciseType.HIGH_INTENSITY_INTERVAL_TRAINING,
        ExerciseSessionRecord.EXERCISE_TYPE_HIKING to ExerciseType.HIKING,
        ExerciseSessionRecord.EXERCISE_TYPE_ICE_HOCKEY to ExerciseType.ICE_HOCKEY,
        ExerciseSessionRecord.EXERCISE_TYPE_ICE_SKATING to ExerciseType.ICE_SKATING,
        ExerciseSessionRecord.EXERCISE_TYPE_MARTIAL_ARTS to ExerciseType.MARTIAL_ARTS,
        ExerciseSessionRecord.EXERCISE_TYPE_PADDLING to ExerciseType.PADDLING,
        ExerciseSessionRecord.EXERCISE_TYPE_PARAGLIDING to ExerciseType.PARAGLIDING,
        ExerciseSessionRecord.EXERCISE_TYPE_PILATES to ExerciseType.PILATES,
        ExerciseSessionRecord.EXERCISE_TYPE_RACQUETBALL to ExerciseType.RACQUETBALL,
        ExerciseSessionRecord.EXERCISE_TYPE_ROCK_CLIMBING to ExerciseType.ROCK_CLIMBING,
        ExerciseSessionRecord.EXERCISE_TYPE_ROLLER_HOCKEY to ExerciseType.ROLLER_HOCKEY,
        ExerciseSessionRecord.EXERCISE_TYPE_ROWING to ExerciseType.ROWING,
        ExerciseSessionRecord.EXERCISE_TYPE_ROWING_MACHINE to ExerciseType.ROWING_MACHINE,
        ExerciseSessionRecord.EXERCISE_TYPE_RUGBY to ExerciseType.RUGBY,
        ExerciseSessionRecord.EXERCISE_TYPE_RUNNING to ExerciseType.RUNNING,
        ExerciseSessionRecord.EXERCISE_TYPE_RUNNING_TREADMILL to ExerciseType.RUNNING_TREADMILL,
        ExerciseSessionRecord.EXERCISE_TYPE_SAILING to ExerciseType.SAILING,
        ExerciseSessionRecord.EXERCISE_TYPE_SCUBA_DIVING to ExerciseType.SCUBA_DIVING,
        ExerciseSessionRecord.EXERCISE_TYPE_SKATING to ExerciseType.SKATING,
        ExerciseSessionRecord.EXERCISE_TYPE_SKIING to ExerciseType.SKIING,
        ExerciseSessionRecord.EXERCISE_TYPE_SNOWBOARDING to ExerciseType.SNOWBOARDING,
        ExerciseSessionRecord.EXERCISE_TYPE_SNOWSHOEING to ExerciseType.SNOWSHOEING,
        ExerciseSessionRecord.EXERCISE_TYPE_SOCCER to ExerciseType.SOCCER,
        ExerciseSessionRecord.EXERCISE_TYPE_SOFTBALL to ExerciseType.SOFTBALL,
        ExerciseSessionRecord.EXERCISE_TYPE_SQUASH to ExerciseType.SQUASH,
        ExerciseSessionRecord.EXERCISE_TYPE_STAIR_CLIMBING to ExerciseType.STAIR_CLIMBING,
        ExerciseSessionRecord.EXERCISE_TYPE_STAIR_CLIMBING_MACHINE to ExerciseType.STAIR_CLIMBING_MACHINE,
        ExerciseSessionRecord.EXERCISE_TYPE_STRENGTH_TRAINING to ExerciseType.STRENGTH_TRAINING,
        ExerciseSessionRecord.EXERCISE_TYPE_STRETCHING to ExerciseType.STRETCHING,
        ExerciseSessionRecord.EXERCISE_TYPE_SURFING to ExerciseType.SURFING,
        ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_OPEN_WATER to ExerciseType.SWIMMING_OPEN_WATER,
        ExerciseSessionRecord.EXERCISE_TYPE_SWIMMING_POOL to ExerciseType.SWIMMING_POOL,
        ExerciseSessionRecord.EXERCISE_TYPE_TABLE_TENNIS to ExerciseType.TABLE_TENNIS,
        ExerciseSessionRecord.EXERCISE_TYPE_TENNIS to ExerciseType.TENNIS,
        ExerciseSessionRecord.EXERCISE_TYPE_VOLLEYBALL to ExerciseType.VOLLEYBALL,
        ExerciseSessionRecord.EXERCISE_TYPE_WALKING to ExerciseType.WALKING,
        ExerciseSessionRecord.EXERCISE_TYPE_WATER_POLO to ExerciseType.WATER_POLO,
        ExerciseSessionRecord.EXERCISE_TYPE_WEIGHTLIFTING to ExerciseType.WEIGHTLIFTING,
        ExerciseSessionRecord.EXERCISE_TYPE_WHEELCHAIR to ExerciseType.WHEELCHAIR,
        ExerciseSessionRecord.EXERCISE_TYPE_YOGA to ExerciseType.YOGA,
    )

internal val ExerciseType.constant: Int
    get() = exerciseTypeConstants.constant(this)

internal val Int.asExerciseType: ExerciseType
    get() = exerciseTypeConstants.entry(this)
