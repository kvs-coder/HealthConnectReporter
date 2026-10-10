package com.kvs.healthconnectreporter.decorator

import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.model.payload.Sample
import com.kvs.healthconnectreporter.model.payloadJson

/** Throws when the sample ends before it starts, which Health Connect rejects. */
internal fun Sample.validateInterval() {
    val end = endTimestamp ?: return
    if (end < startTimestamp) {
        throw HealthConnectException.InvalidValue(
            "Invalid ${healthType.identifier}: endTimestamp $end is before startTimestamp $startTimestamp",
        )
    }
}

/** Throws when a nested interval (stage, segment, lap) ends before it starts. */
internal fun Long.validateInterval(end: Long) {
    if (end < this) throw HealthConnectException.InvalidValue("Invalid interval: end $end is before start $this")
}

/** Throws when a nested series value or interval lies outside the sample, which Health Connect rejects. */
internal fun Sample.validateInside(
    sampleEnd: Long,
    start: Long,
    end: Long = start,
) {
    if (start < startTimestamp || end > sampleEnd) {
        throw HealthConnectException.InvalidValue(
            "Invalid ${healthType.identifier}: $start–$end is outside $startTimestamp–$sampleEnd",
        )
    }
}

/** JSON of a sample, with its `type` discriminator so it can be decoded as any **Sample**. */
internal val Sample.encoded: String
    get() = payloadJson.encodeToString(Sample.serializer(), this)
