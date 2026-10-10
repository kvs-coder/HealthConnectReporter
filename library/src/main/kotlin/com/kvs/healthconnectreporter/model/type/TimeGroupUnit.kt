package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/** **TimeGroupUnit** the calendar unit aggregate results are grouped by, in local time */
@Serializable
public enum class TimeGroupUnit {
    DAILY,
    WEEKLY,
    MONTHLY,
    YEARLY,
}
