package com.kvs.healthconnectreporter.model

import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.model.type.TimeGroupUnit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DateTimeException
import java.time.ZoneId

/** **TimeGroup** how aggregate results are grouped into buckets; JSON carries a `type` discriminator */
@Serializable
public sealed interface TimeGroup {
    /**
     * **Duration** buckets of a fixed physical duration, e.g. every 15 minutes.
     *
     * @param durationMillis **Long** bucket length in milliseconds, at least 1
     * @throws HealthConnectException.InvalidValue when [durationMillis] is less than 1
     */
    @Serializable
    @SerialName("duration")
    public data class Duration(
        val durationMillis: Long,
    ) : TimeGroup {
        init {
            if (durationMillis < 1) {
                throw HealthConnectException.InvalidValue("Invalid TimeGroup duration: $durationMillis")
            }
        }
    }

    /**
     * **Period** buckets of calendar periods in a time zone, e.g. days or months.
     *
     * @param unit **TimeGroupUnit** the calendar unit
     * @param count **Int** units per bucket, at least 1. 1 by default
     * @param zoneId **String** the zone of the calendar, e.g. `Europe/Berlin`. The device's zone by default
     * @throws HealthConnectException.InvalidValue when [count] is less than 1 or [zoneId] is not a zone
     */
    @Serializable
    @SerialName("period")
    public data class Period(
        val unit: TimeGroupUnit,
        val count: Int = 1,
        val zoneId: String = ZoneId.systemDefault().id,
    ) : TimeGroup {
        init {
            if (count < 1) throw HealthConnectException.InvalidValue("Invalid TimeGroup count: $count")
            try {
                ZoneId.of(zoneId)
            } catch (exception: DateTimeException) {
                throw HealthConnectException.InvalidValue("Invalid TimeGroup zoneId: $zoneId", exception)
            }
        }
    }
}
