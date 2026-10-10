package com.kvs.healthconnectreporter.model

import com.kvs.healthconnectreporter.model.payload.Sample
import kotlinx.serialization.Serializable

/**
 * **ChangeSet** the changes since an anchor.
 *
 * @param upserted **List<Sample>** inserted or updated samples, oldest change first
 * @param deletedIds **List<String>** Health Connect ids of deleted records
 * @param anchor **Anchor** the anchor after these changes; pass it to the next call
 */
@Serializable
public data class ChangeSet(
    val upserted: List<Sample>,
    val deletedIds: List<String>,
    val anchor: Anchor,
) : Payload {
    /** **Boolean** there were no changes */
    val isEmpty: Boolean get() = upserted.isEmpty() && deletedIds.isEmpty()

    override val json: String get() = Companion.encode(this)

    /** Factory of **ChangeSet** */
    public companion object : Payload.Factory<ChangeSet>({ ChangeSet.serializer() })
}
