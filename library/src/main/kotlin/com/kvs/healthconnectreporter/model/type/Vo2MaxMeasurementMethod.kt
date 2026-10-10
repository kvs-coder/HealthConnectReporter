package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **Vo2MaxMeasurementMethod** how VO2 max was measured.
 *
 * Mirrors Health Connect's `Vo2MaxRecord.MEASUREMENT_METHOD_*` constants entry for entry.
 */
@Serializable
public enum class Vo2MaxMeasurementMethod {
    OTHER,
    METABOLIC_CART,
    HEART_RATE_RATIO,
    COOPER_TEST,
    MULTISTAGE_FITNESS_TEST,
    ROCKPORT_FITNESS_TEST,
}
