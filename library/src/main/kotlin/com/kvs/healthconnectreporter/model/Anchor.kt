package com.kvs.healthconnectreporter.model

import kotlinx.serialization.Serializable

/**
 * **Anchor** a position in Health Connect's change history; store it to read only later changes.
 *
 * Health Connect keeps changes for 30 days; an older anchor makes **HealthConnectObserver.changes** throw
 * **HealthConnectException.AnchorExpired**.
 *
 * @param token **String** the Health Connect changes token
 */
@Serializable
public data class Anchor(
    val token: String,
) : Payload {
    override val json: String get() = Companion.encode(this)

    /** Factory of **Anchor** */
    public companion object : Payload.Factory<Anchor>({ Anchor.serializer() })
}
