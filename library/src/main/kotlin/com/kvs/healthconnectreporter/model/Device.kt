package com.kvs.healthconnectreporter.model

import com.kvs.healthconnectreporter.model.type.DeviceType
import kotlinx.serialization.Serializable

/**
 * **Device** the device that recorded a sample.
 *
 * @param type **DeviceType** the kind of device. [DeviceType.UNKNOWN] by default
 * @param manufacturer **String?** manufacturer, e.g. `Google`
 * @param model **String?** model, e.g. `Pixel Watch`
 */
@Serializable
public data class Device(
    val type: DeviceType = DeviceType.UNKNOWN,
    val manufacturer: String? = null,
    val model: String? = null,
) : Payload {
    override val json: String get() = Companion.encode(this)

    /** Factory of **Device** */
    public companion object : Payload.Factory<Device>({ Device.serializer() })
}
