package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.SkinTemperatureRecord
import com.kvs.healthconnectreporter.model.type.SkinTemperatureMeasurementLocation

private val skinTemperatureMeasurementLocationConstants =
    ConstantFamily(
        SkinTemperatureMeasurementLocation.UNKNOWN,
        SkinTemperatureRecord.MEASUREMENT_LOCATION_UNKNOWN to SkinTemperatureMeasurementLocation.UNKNOWN,
        SkinTemperatureRecord.MEASUREMENT_LOCATION_FINGER to SkinTemperatureMeasurementLocation.FINGER,
        SkinTemperatureRecord.MEASUREMENT_LOCATION_TOE to SkinTemperatureMeasurementLocation.TOE,
        SkinTemperatureRecord.MEASUREMENT_LOCATION_WRIST to SkinTemperatureMeasurementLocation.WRIST,
    )

internal val SkinTemperatureMeasurementLocation.constant: Int
    get() = skinTemperatureMeasurementLocationConstants.constant(this)

internal val Int.asSkinTemperatureMeasurementLocation: SkinTemperatureMeasurementLocation
    get() = skinTemperatureMeasurementLocationConstants.entry(this)
