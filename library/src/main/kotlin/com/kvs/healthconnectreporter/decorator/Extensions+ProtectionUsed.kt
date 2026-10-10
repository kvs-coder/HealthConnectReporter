package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.SexualActivityRecord
import com.kvs.healthconnectreporter.model.type.ProtectionUsed

private val protectionUsedConstants =
    ConstantFamily(
        ProtectionUsed.UNKNOWN,
        SexualActivityRecord.PROTECTION_USED_UNKNOWN to ProtectionUsed.UNKNOWN,
        SexualActivityRecord.PROTECTION_USED_PROTECTED to ProtectionUsed.PROTECTED,
        SexualActivityRecord.PROTECTION_USED_UNPROTECTED to ProtectionUsed.UNPROTECTED,
    )

internal val ProtectionUsed.constant: Int
    get() = protectionUsedConstants.constant(this)

internal val Int.asProtectionUsed: ProtectionUsed
    get() = protectionUsedConstants.entry(this)
