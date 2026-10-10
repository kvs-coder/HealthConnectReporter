package com.kvs.healthconnectreporter.decorator

import com.kvs.healthconnectreporter.model.Device
import androidx.health.connect.client.records.metadata.Device as OriginalDevice

internal val Device.asOriginal: OriginalDevice
    get() = OriginalDevice(type = type.constant, manufacturer = manufacturer, model = model)

internal val OriginalDevice.harmonized: Device
    get() = Device(type = type.asDeviceType, manufacturer = manufacturer, model = model)
