package com.kvs.healthconnectreporter.decorator

import java.time.Duration

internal val Long.asDuration: Duration
    get() = Duration.ofMillis(this)

internal val Duration.millis: Long
    get() = toMillis()
