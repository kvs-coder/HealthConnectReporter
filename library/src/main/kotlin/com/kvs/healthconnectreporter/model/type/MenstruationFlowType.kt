package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **MenstruationFlowType** the menstrual flow.
 *
 * Mirrors Health Connect's `MenstruationFlowRecord.FLOW_*` constants entry for entry.
 */
@Serializable
public enum class MenstruationFlowType {
    UNKNOWN,
    LIGHT,
    MEDIUM,
    HEAVY,
}
