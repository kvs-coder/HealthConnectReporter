package com.kvs.healthconnectreporter.decorator

import java.time.ZoneOffset

internal val Int?.asZoneOffset: ZoneOffset?
    get() = this?.let(ZoneOffset::ofTotalSeconds)

internal val ZoneOffset?.seconds: Int?
    get() = this?.totalSeconds
