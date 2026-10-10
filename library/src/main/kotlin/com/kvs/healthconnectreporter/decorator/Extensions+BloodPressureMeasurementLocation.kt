package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.BloodPressureRecord
import com.kvs.healthconnectreporter.model.type.BloodPressureMeasurementLocation

private val bloodPressureMeasurementLocationConstants =
    ConstantFamily(
        BloodPressureMeasurementLocation.UNKNOWN,
        BloodPressureRecord.MEASUREMENT_LOCATION_UNKNOWN to BloodPressureMeasurementLocation.UNKNOWN,
        BloodPressureRecord.MEASUREMENT_LOCATION_LEFT_WRIST to BloodPressureMeasurementLocation.LEFT_WRIST,
        BloodPressureRecord.MEASUREMENT_LOCATION_RIGHT_WRIST to BloodPressureMeasurementLocation.RIGHT_WRIST,
        BloodPressureRecord.MEASUREMENT_LOCATION_LEFT_UPPER_ARM to BloodPressureMeasurementLocation.LEFT_UPPER_ARM,
        BloodPressureRecord.MEASUREMENT_LOCATION_RIGHT_UPPER_ARM to BloodPressureMeasurementLocation.RIGHT_UPPER_ARM,
    )

internal val BloodPressureMeasurementLocation.constant: Int
    get() = bloodPressureMeasurementLocationConstants.constant(this)

internal val Int.asBloodPressureMeasurementLocation: BloodPressureMeasurementLocation
    get() = bloodPressureMeasurementLocationConstants.entry(this)
