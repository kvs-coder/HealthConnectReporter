package com.kvs.healthconnectreporter.decorator

import java.time.Instant

internal val Long.asInstant: Instant
    get() = Instant.ofEpochMilli(this)

internal val Instant.epochMillis: Long
    get() = toEpochMilli()
