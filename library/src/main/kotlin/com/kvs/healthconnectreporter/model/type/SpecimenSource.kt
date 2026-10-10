package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **SpecimenSource** the specimen a blood glucose level was measured in.
 *
 * Mirrors Health Connect's `BloodGlucoseRecord.SPECIMEN_SOURCE_*` constants entry for entry.
 */
@Serializable
public enum class SpecimenSource {
    UNKNOWN,
    INTERSTITIAL_FLUID,
    CAPILLARY_BLOOD,
    PLASMA,
    SERUM,
    TEARS,
    WHOLE_BLOOD,
}
