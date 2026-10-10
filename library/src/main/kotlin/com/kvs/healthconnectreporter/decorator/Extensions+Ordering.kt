package com.kvs.healthconnectreporter.decorator

import com.kvs.healthconnectreporter.model.Ordering

internal val Ordering.ascending: Boolean
    get() =
        when (this) {
            Ordering.ASCENDING -> true
            Ordering.DESCENDING -> false
        }
