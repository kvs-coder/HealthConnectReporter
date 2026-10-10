package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **DeviceType** the kind of device that recorded a sample.
 *
 * Mirrors Health Connect's `Device.TYPE_*` constants entry for entry.
 */
@Serializable
public enum class DeviceType {
    UNKNOWN,
    WATCH,
    PHONE,
    SCALE,
    RING,
    HEAD_MOUNTED,
    FITNESS_BAND,
    CHEST_STRAP,
    SMART_DISPLAY,
}
