package com.kvs.healthconnectreporter.model

import com.kvs.healthconnectreporter.model.payload.Sample
import kotlinx.serialization.Serializable

/**
 * **Page** one page of read results.
 *
 * @param items **List<Sample>** the samples of this page
 * @param nextPageToken **String?** pass to the next call to get the next page; null on the last page
 */
@Serializable
public data class Page(
    val items: List<Sample>,
    val nextPageToken: String? = null,
) : Payload {
    override val json: String get() = Companion.encode(this)

    /** Factory of **Page** */
    public companion object : Payload.Factory<Page>({ Page.serializer() })
}
