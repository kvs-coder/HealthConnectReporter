package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **BloodPressureMeasurementLocation** where blood pressure was measured.
 *
 * Mirrors Health Connect's `BloodPressureRecord.MEASUREMENT_LOCATION_*` constants entry for entry.
 */
@Serializable
public enum class BloodPressureMeasurementLocation {
    UNKNOWN,
    LEFT_WRIST,
    RIGHT_WRIST,
    LEFT_UPPER_ARM,
    RIGHT_UPPER_ARM,
}
