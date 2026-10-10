package com.kvs.healthconnectreporter.decorator

import com.kvs.healthconnectreporter.model.DataOrigin
import androidx.health.connect.client.records.metadata.DataOrigin as OriginalDataOrigin

internal val DataOrigin.asOriginal: OriginalDataOrigin
    get() = OriginalDataOrigin(packageName)

internal val Set<DataOrigin>.asOriginal: Set<OriginalDataOrigin>
    get() = mapTo(mutableSetOf()) { it.asOriginal }
