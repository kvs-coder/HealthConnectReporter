package com.kvs.healthconnectreporter.decorator

import com.kvs.healthconnectreporter.model.type.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation as OriginalLocation

private val bodyTemperatureMeasurementLocationConstants =
    ConstantFamily(
        BodyTemperatureMeasurementLocation.UNKNOWN,
        OriginalLocation.MEASUREMENT_LOCATION_UNKNOWN to BodyTemperatureMeasurementLocation.UNKNOWN,
        OriginalLocation.MEASUREMENT_LOCATION_ARMPIT to BodyTemperatureMeasurementLocation.ARMPIT,
        OriginalLocation.MEASUREMENT_LOCATION_FINGER to BodyTemperatureMeasurementLocation.FINGER,
        OriginalLocation.MEASUREMENT_LOCATION_FOREHEAD to BodyTemperatureMeasurementLocation.FOREHEAD,
        OriginalLocation.MEASUREMENT_LOCATION_MOUTH to BodyTemperatureMeasurementLocation.MOUTH,
        OriginalLocation.MEASUREMENT_LOCATION_RECTUM to BodyTemperatureMeasurementLocation.RECTUM,
        OriginalLocation.MEASUREMENT_LOCATION_TEMPORAL_ARTERY to BodyTemperatureMeasurementLocation.TEMPORAL_ARTERY,
        OriginalLocation.MEASUREMENT_LOCATION_TOE to BodyTemperatureMeasurementLocation.TOE,
        OriginalLocation.MEASUREMENT_LOCATION_EAR to BodyTemperatureMeasurementLocation.EAR,
        OriginalLocation.MEASUREMENT_LOCATION_WRIST to BodyTemperatureMeasurementLocation.WRIST,
        OriginalLocation.MEASUREMENT_LOCATION_VAGINA to BodyTemperatureMeasurementLocation.VAGINA,
    )

internal val BodyTemperatureMeasurementLocation.constant: Int
    get() = bodyTemperatureMeasurementLocationConstants.constant(this)

internal val Int.asBodyTemperatureMeasurementLocation: BodyTemperatureMeasurementLocation
    get() = bodyTemperatureMeasurementLocationConstants.entry(this)
