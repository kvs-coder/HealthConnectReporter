package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.time.TimeRangeFilter
import com.kvs.healthconnectreporter.model.TimeRange
import java.time.ZoneId

internal val TimeRange.asTimeRangeFilter: TimeRangeFilter
    get() = TimeRangeFilter.between(startTimestamp.asInstant, endTimestamp.asInstant)

/** The range as local date-times in [zoneId], for calendar-period aggregation. */
internal fun TimeRange.asLocalTimeRangeFilter(zoneId: ZoneId): TimeRangeFilter =
    TimeRangeFilter.between(
        startTimestamp.asInstant.atZone(zoneId).toLocalDateTime(),
        endTimestamp.asInstant.atZone(zoneId).toLocalDateTime(),
    )
