package com.kvs.healthconnectreporter.decorator

import com.kvs.healthconnectreporter.model.TimeGroup
import com.kvs.healthconnectreporter.model.type.TimeGroupUnit
import java.time.Period

internal val TimeGroup.Period.asPeriod: Period
    get() =
        when (unit) {
            TimeGroupUnit.DAILY -> Period.ofDays(count)
            TimeGroupUnit.WEEKLY -> Period.ofWeeks(count)
            TimeGroupUnit.MONTHLY -> Period.ofMonths(count)
            TimeGroupUnit.YEARLY -> Period.ofYears(count)
        }
