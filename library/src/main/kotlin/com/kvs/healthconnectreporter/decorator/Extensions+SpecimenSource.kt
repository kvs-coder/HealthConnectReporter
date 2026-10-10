package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.BloodGlucoseRecord
import com.kvs.healthconnectreporter.model.type.SpecimenSource

private val specimenSourceConstants =
    ConstantFamily(
        SpecimenSource.UNKNOWN,
        BloodGlucoseRecord.SPECIMEN_SOURCE_UNKNOWN to SpecimenSource.UNKNOWN,
        BloodGlucoseRecord.SPECIMEN_SOURCE_INTERSTITIAL_FLUID to SpecimenSource.INTERSTITIAL_FLUID,
        BloodGlucoseRecord.SPECIMEN_SOURCE_CAPILLARY_BLOOD to SpecimenSource.CAPILLARY_BLOOD,
        BloodGlucoseRecord.SPECIMEN_SOURCE_PLASMA to SpecimenSource.PLASMA,
        BloodGlucoseRecord.SPECIMEN_SOURCE_SERUM to SpecimenSource.SERUM,
        BloodGlucoseRecord.SPECIMEN_SOURCE_TEARS to SpecimenSource.TEARS,
        BloodGlucoseRecord.SPECIMEN_SOURCE_WHOLE_BLOOD to SpecimenSource.WHOLE_BLOOD,
    )

internal val SpecimenSource.constant: Int
    get() = specimenSourceConstants.constant(this)

internal val Int.asSpecimenSource: SpecimenSource
    get() = specimenSourceConstants.entry(this)
