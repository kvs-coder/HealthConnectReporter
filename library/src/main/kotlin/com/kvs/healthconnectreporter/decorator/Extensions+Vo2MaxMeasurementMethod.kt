package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.Vo2MaxRecord
import com.kvs.healthconnectreporter.model.type.Vo2MaxMeasurementMethod

private val vo2MaxMeasurementMethodConstants =
    ConstantFamily(
        Vo2MaxMeasurementMethod.OTHER,
        Vo2MaxRecord.MEASUREMENT_METHOD_OTHER to Vo2MaxMeasurementMethod.OTHER,
        Vo2MaxRecord.MEASUREMENT_METHOD_METABOLIC_CART to Vo2MaxMeasurementMethod.METABOLIC_CART,
        Vo2MaxRecord.MEASUREMENT_METHOD_HEART_RATE_RATIO to Vo2MaxMeasurementMethod.HEART_RATE_RATIO,
        Vo2MaxRecord.MEASUREMENT_METHOD_COOPER_TEST to Vo2MaxMeasurementMethod.COOPER_TEST,
        Vo2MaxRecord.MEASUREMENT_METHOD_MULTISTAGE_FITNESS_TEST to Vo2MaxMeasurementMethod.MULTISTAGE_FITNESS_TEST,
        Vo2MaxRecord.MEASUREMENT_METHOD_ROCKPORT_FITNESS_TEST to Vo2MaxMeasurementMethod.ROCKPORT_FITNESS_TEST,
    )

internal val Vo2MaxMeasurementMethod.constant: Int
    get() = vo2MaxMeasurementMethodConstants.constant(this)

internal val Int.asVo2MaxMeasurementMethod: Vo2MaxMeasurementMethod
    get() = vo2MaxMeasurementMethodConstants.entry(this)
