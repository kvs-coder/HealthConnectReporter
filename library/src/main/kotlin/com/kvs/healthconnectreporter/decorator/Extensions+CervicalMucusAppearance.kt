package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.CervicalMucusRecord
import com.kvs.healthconnectreporter.model.type.CervicalMucusAppearance

private val cervicalMucusAppearanceConstants =
    ConstantFamily(
        CervicalMucusAppearance.UNKNOWN,
        CervicalMucusRecord.APPEARANCE_UNKNOWN to CervicalMucusAppearance.UNKNOWN,
        CervicalMucusRecord.APPEARANCE_DRY to CervicalMucusAppearance.DRY,
        CervicalMucusRecord.APPEARANCE_STICKY to CervicalMucusAppearance.STICKY,
        CervicalMucusRecord.APPEARANCE_CREAMY to CervicalMucusAppearance.CREAMY,
        CervicalMucusRecord.APPEARANCE_WATERY to CervicalMucusAppearance.WATERY,
        CervicalMucusRecord.APPEARANCE_EGG_WHITE to CervicalMucusAppearance.EGG_WHITE,
        CervicalMucusRecord.APPEARANCE_UNUSUAL to CervicalMucusAppearance.UNUSUAL,
    )

internal val CervicalMucusAppearance.constant: Int
    get() = cervicalMucusAppearanceConstants.constant(this)

internal val Int.asCervicalMucusAppearance: CervicalMucusAppearance
    get() = cervicalMucusAppearanceConstants.entry(this)
