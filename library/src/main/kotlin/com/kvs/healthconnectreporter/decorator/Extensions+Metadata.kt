package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.metadata.Metadata
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import java.time.Instant

/** **String?** the record id, null for a record that is not stored yet */
internal val Metadata.sampleId: String?
    get() = id.ifEmpty { null }

/** **Long?** the client record version, meaningful only with a client record id */
internal val Metadata.sampleClientRecordVersion: Long?
    get() = clientRecordVersion.takeIf { clientRecordId != null }

internal val Metadata.sampleRecordingMethod: RecordingMethod
    get() = recordingMethod.asRecordingMethod

internal val Metadata.sampleDevice: Device?
    get() = device?.harmonized

internal val Metadata.sampleDataOrigin: DataOrigin?
    get() = dataOrigin.packageName.ifEmpty { null }?.let(::DataOrigin)

internal val Metadata.lastModifiedTimestamp: Long?
    get() = lastModifiedTime.takeIf { it != Instant.EPOCH }?.epochMillis
