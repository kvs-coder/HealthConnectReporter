package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.SleepSessionRecord
import com.kvs.healthconnectreporter.model.type.SleepStageType

private val sleepStageTypeConstants =
    ConstantFamily(
        SleepStageType.UNKNOWN,
        SleepSessionRecord.STAGE_TYPE_UNKNOWN to SleepStageType.UNKNOWN,
        SleepSessionRecord.STAGE_TYPE_AWAKE to SleepStageType.AWAKE,
        SleepSessionRecord.STAGE_TYPE_SLEEPING to SleepStageType.SLEEPING,
        SleepSessionRecord.STAGE_TYPE_OUT_OF_BED to SleepStageType.OUT_OF_BED,
        SleepSessionRecord.STAGE_TYPE_LIGHT to SleepStageType.LIGHT,
        SleepSessionRecord.STAGE_TYPE_DEEP to SleepStageType.DEEP,
        SleepSessionRecord.STAGE_TYPE_REM to SleepStageType.REM,
        SleepSessionRecord.STAGE_TYPE_AWAKE_IN_BED to SleepStageType.AWAKE_IN_BED,
    )

internal val SleepStageType.constant: Int
    get() = sleepStageTypeConstants.constant(this)

internal val Int.asSleepStageType: SleepStageType
    get() = sleepStageTypeConstants.entry(this)
