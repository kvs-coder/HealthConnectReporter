package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **CervicalMucusAppearance** the appearance of cervical mucus.
 *
 * Mirrors Health Connect's `CervicalMucusRecord.APPEARANCE_*` constants entry for entry.
 */
@Serializable
public enum class CervicalMucusAppearance {
    UNKNOWN,
    DRY,
    STICKY,
    CREAMY,
    WATERY,
    EGG_WHITE,
    UNUSUAL,
}
