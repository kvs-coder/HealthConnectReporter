package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.BloodPressureRecord
import com.kvs.healthconnectreporter.model.type.BodyPosition

private val bodyPositionConstants =
    ConstantFamily(
        BodyPosition.UNKNOWN,
        BloodPressureRecord.BODY_POSITION_UNKNOWN to BodyPosition.UNKNOWN,
        BloodPressureRecord.BODY_POSITION_STANDING_UP to BodyPosition.STANDING_UP,
        BloodPressureRecord.BODY_POSITION_SITTING_DOWN to BodyPosition.SITTING_DOWN,
        BloodPressureRecord.BODY_POSITION_LYING_DOWN to BodyPosition.LYING_DOWN,
        BloodPressureRecord.BODY_POSITION_RECLINING to BodyPosition.RECLINING,
    )

internal val BodyPosition.constant: Int
    get() = bodyPositionConstants.constant(this)

internal val Int.asBodyPosition: BodyPosition
    get() = bodyPositionConstants.entry(this)
