package com.kvs.healthconnectreporter.model

import com.kvs.healthconnectreporter.HealthConnectException
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId

/**
 * **TimeRange** a time range in epoch milliseconds, end exclusive.
 *
 * @param startTimestamp **Long** epoch milliseconds, inclusive
 * @param endTimestamp **Long** epoch milliseconds, exclusive
 * @throws HealthConnectException.InvalidValue when [endTimestamp] is not after [startTimestamp]
 */
@Serializable
public data class TimeRange(
    val startTimestamp: Long,
    val endTimestamp: Long,
) : Payload {
    init {
        if (endTimestamp <= startTimestamp) {
            throw HealthConnectException.InvalidValue(
                "Invalid TimeRange: endTimestamp $endTimestamp is not after startTimestamp $startTimestamp",
            )
        }
    }

    override val json: String get() = Companion.encode(this)

    /** Factory of **TimeRange** */
    public companion object : Payload.Factory<TimeRange>({ TimeRange.serializer() }) {
        /**
         * The calendar day that contains [time].
         *
         * @param time **Long** epoch milliseconds. Now by default
         * @param zoneId **ZoneId** the zone of the day. The device's zone by default
         * @return **TimeRange** from midnight to the next midnight
         */
        public fun day(
            time: Long = System.currentTimeMillis(),
            zoneId: ZoneId = ZoneId.systemDefault(),
        ): TimeRange = lastDays(1, time, zoneId)

        /**
         * The last [days] calendar days, including today.
         *
         * @param days **Int** number of days, at least 1
         * @param time **Long** epoch milliseconds inside the last day. Now by default
         * @param zoneId **ZoneId** the zone of the days. The device's zone by default
         * @return **TimeRange** from midnight [days] - 1 days ago to the next midnight
         * @throws HealthConnectException.InvalidValue when [days] is less than 1
         */
        public fun lastDays(
            days: Int,
            time: Long = System.currentTimeMillis(),
            zoneId: ZoneId = ZoneId.systemDefault(),
        ): TimeRange {
            if (days < 1) throw HealthConnectException.InvalidValue("Invalid days: $days")
            val today =
                Instant
                    .ofEpochMilli(time)
                    .atZone(zoneId)
                    .toLocalDate()
            val start =
                today
                    .minusDays(days - 1L)
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()
            val end =
                today
                    .plusDays(1)
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()
            return TimeRange(start, end)
        }
    }
}
