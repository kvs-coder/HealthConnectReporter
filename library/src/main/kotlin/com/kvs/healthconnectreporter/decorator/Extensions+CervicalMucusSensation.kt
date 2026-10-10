package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.CervicalMucusRecord
import com.kvs.healthconnectreporter.model.type.CervicalMucusSensation

private val cervicalMucusSensationConstants =
    ConstantFamily(
        CervicalMucusSensation.UNKNOWN,
        CervicalMucusRecord.SENSATION_UNKNOWN to CervicalMucusSensation.UNKNOWN,
        CervicalMucusRecord.SENSATION_LIGHT to CervicalMucusSensation.LIGHT,
        CervicalMucusRecord.SENSATION_MEDIUM to CervicalMucusSensation.MEDIUM,
        CervicalMucusRecord.SENSATION_HEAVY to CervicalMucusSensation.HEAVY,
    )

internal val CervicalMucusSensation.constant: Int
    get() = cervicalMucusSensationConstants.constant(this)

internal val Int.asCervicalMucusSensation: CervicalMucusSensation
    get() = cervicalMucusSensationConstants.entry(this)
