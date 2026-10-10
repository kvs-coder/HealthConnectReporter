package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **SkinTemperatureMeasurementLocation** where skin temperature was measured.
 *
 * Mirrors Health Connect's `SkinTemperatureRecord.MEASUREMENT_LOCATION_*` constants entry for entry.
 */
@Serializable
public enum class SkinTemperatureMeasurementLocation {
    UNKNOWN,
    FINGER,
    TOE,
    WRIST,
}
