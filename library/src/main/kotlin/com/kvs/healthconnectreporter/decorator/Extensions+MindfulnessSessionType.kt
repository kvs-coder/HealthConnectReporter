@file:OptIn(ExperimentalMindfulnessSessionApi::class)

package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.feature.ExperimentalMindfulnessSessionApi
import androidx.health.connect.client.records.MindfulnessSessionRecord
import com.kvs.healthconnectreporter.model.type.MindfulnessSessionType

private val mindfulnessSessionTypeConstants =
    ConstantFamily(
        MindfulnessSessionType.UNKNOWN,
        MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_UNKNOWN to MindfulnessSessionType.UNKNOWN,
        MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_MEDITATION to MindfulnessSessionType.MEDITATION,
        MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_BREATHING to MindfulnessSessionType.BREATHING,
        MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_MUSIC to MindfulnessSessionType.MUSIC,
        MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_MOVEMENT to MindfulnessSessionType.MOVEMENT,
        MindfulnessSessionRecord.MINDFULNESS_SESSION_TYPE_UNGUIDED to MindfulnessSessionType.UNGUIDED,
    )

internal val MindfulnessSessionType.constant: Int
    get() = mindfulnessSessionTypeConstants.constant(this)

internal val Int.asMindfulnessSessionType: MindfulnessSessionType
    get() = mindfulnessSessionTypeConstants.entry(this)
