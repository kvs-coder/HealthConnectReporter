package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **BodyTemperatureMeasurementLocation** where a body temperature was measured.
 *
 * Mirrors Health Connect's `BodyTemperatureMeasurementLocation.MEASUREMENT_LOCATION_*` constants entry for entry.
 */
@Serializable
public enum class BodyTemperatureMeasurementLocation {
    UNKNOWN,
    ARMPIT,
    FINGER,
    FOREHEAD,
    MOUTH,
    RECTUM,
    TEMPORAL_ARTERY,
    TOE,
    EAR,
    WRIST,
    VAGINA,
}
