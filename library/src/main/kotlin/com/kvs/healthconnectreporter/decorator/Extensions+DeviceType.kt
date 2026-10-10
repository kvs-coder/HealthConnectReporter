package com.kvs.healthconnectreporter.decorator

import com.kvs.healthconnectreporter.model.type.DeviceType
import androidx.health.connect.client.records.metadata.Device as OriginalDevice

private val deviceTypeConstants =
    ConstantFamily(
        DeviceType.UNKNOWN,
        OriginalDevice.TYPE_UNKNOWN to DeviceType.UNKNOWN,
        OriginalDevice.TYPE_WATCH to DeviceType.WATCH,
        OriginalDevice.TYPE_PHONE to DeviceType.PHONE,
        OriginalDevice.TYPE_SCALE to DeviceType.SCALE,
        OriginalDevice.TYPE_RING to DeviceType.RING,
        OriginalDevice.TYPE_HEAD_MOUNTED to DeviceType.HEAD_MOUNTED,
        OriginalDevice.TYPE_FITNESS_BAND to DeviceType.FITNESS_BAND,
        OriginalDevice.TYPE_CHEST_STRAP to DeviceType.CHEST_STRAP,
        OriginalDevice.TYPE_SMART_DISPLAY to DeviceType.SMART_DISPLAY,
    )

internal val DeviceType.constant: Int
    get() = deviceTypeConstants.constant(this)

internal val Int.asDeviceType: DeviceType
    get() = deviceTypeConstants.entry(this)
