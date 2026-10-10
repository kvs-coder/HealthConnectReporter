package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.MenstruationFlowRecord
import com.kvs.healthconnectreporter.model.type.MenstruationFlowType

private val menstruationFlowTypeConstants =
    ConstantFamily(
        MenstruationFlowType.UNKNOWN,
        MenstruationFlowRecord.FLOW_UNKNOWN to MenstruationFlowType.UNKNOWN,
        MenstruationFlowRecord.FLOW_LIGHT to MenstruationFlowType.LIGHT,
        MenstruationFlowRecord.FLOW_MEDIUM to MenstruationFlowType.MEDIUM,
        MenstruationFlowRecord.FLOW_HEAVY to MenstruationFlowType.HEAVY,
    )

internal val MenstruationFlowType.constant: Int
    get() = menstruationFlowTypeConstants.constant(this)

internal val Int.asMenstruationFlowType: MenstruationFlowType
    get() = menstruationFlowTypeConstants.entry(this)
