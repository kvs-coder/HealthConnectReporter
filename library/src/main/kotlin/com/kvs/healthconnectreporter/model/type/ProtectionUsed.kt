package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **ProtectionUsed** whether protection was used during sexual activity.
 *
 * Mirrors Health Connect's `SexualActivityRecord.PROTECTION_USED_*` constants entry for entry.
 */
@Serializable
public enum class ProtectionUsed {
    UNKNOWN,
    PROTECTED,
    UNPROTECTED,
}
