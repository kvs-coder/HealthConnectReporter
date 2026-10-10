package com.kvs.healthconnectreporter.model

import kotlinx.serialization.Serializable

/**
 * **DataOrigin** the app that wrote a record.
 *
 * @param packageName **String** the app's package name, e.g. `com.google.android.apps.fitness`
 */
@Serializable
public data class DataOrigin(
    val packageName: String,
) : Payload {
    override val json: String get() = Companion.encode(this)

    /** Factory of **DataOrigin** */
    public companion object : Payload.Factory<DataOrigin>({ DataOrigin.serializer() })
}
