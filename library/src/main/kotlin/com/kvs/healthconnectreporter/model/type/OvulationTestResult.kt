package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **OvulationTestResult** the result of an ovulation test.
 *
 * Mirrors Health Connect's `OvulationTestRecord.RESULT_*` constants entry for entry.
 */
@Serializable
public enum class OvulationTestResult {
    INCONCLUSIVE,
    POSITIVE,
    HIGH,
    NEGATIVE,
}
