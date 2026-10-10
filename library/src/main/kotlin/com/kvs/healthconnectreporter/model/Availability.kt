package com.kvs.healthconnectreporter.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** **Availability** of Health Connect on this device */
@Serializable
public enum class Availability {
    /** Health Connect is ready to use. */
    @SerialName("available")
    AVAILABLE,

    /** The Health Connect app must be installed or updated from Google Play first (Android 13 and lower). */
    @SerialName("update_required")
    UPDATE_REQUIRED,

    /** Health Connect does not run on this device (Android 8 and lower, or an unsupported profile). */
    @SerialName("unavailable")
    UNAVAILABLE,
}
