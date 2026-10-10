package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **CervicalMucusSensation** the sensation of cervical mucus.
 *
 * Mirrors Health Connect's `CervicalMucusRecord.SENSATION_*` constants entry for entry.
 */
@Serializable
public enum class CervicalMucusSensation {
    UNKNOWN,
    LIGHT,
    MEDIUM,
    HEAVY,
}
