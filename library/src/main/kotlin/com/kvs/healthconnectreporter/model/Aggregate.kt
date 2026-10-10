package com.kvs.healthconnectreporter.model

import com.kvs.healthconnectreporter.model.type.Aggregation
import kotlinx.serialization.Serializable

/**
 * **Aggregate** one aggregated value for a time bucket.
 *
 * @param aggregation **Aggregation** the aggregated metric
 * @param startTimestamp **Long** bucket start, epoch milliseconds
 * @param endTimestamp **Long** bucket end, epoch milliseconds
 * @param value **Double?** the value in [unit]; null when Health Connect has no data for the bucket
 * @param unit **String** unit of [value]. [Aggregation.unit] by default
 */
@Serializable
public data class Aggregate(
    val aggregation: Aggregation,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val value: Double?,
    val unit: String = aggregation.unit,
) : Payload {
    override val json: String get() = Companion.encode(this)

    /** Factory of **Aggregate** */
    public companion object : Payload.Factory<Aggregate>({ Aggregate.serializer() })
}
