package com.kvs.healthconnectreporter.model.payload

import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Payload
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import kotlinx.serialization.Serializable

/**
 * **Sample** one record stored in Health Connect.
 *
 * In JSON a sample carries a `type` discriminator with its [healthType] identifier.
 */
@Serializable
public sealed interface Sample : Payload {
    /** **Long** start time (the measurement time of an instantaneous record), epoch milliseconds */
    public val startTimestamp: Long

    /** **Long?** end time, epoch milliseconds; null for instantaneous records */
    public val endTimestamp: Long?

    /** **String?** Health Connect id of the stored record; null until it is inserted */
    public val id: String?

    /** **String?** your own id for the record, kept by Health Connect; inserting it again updates the record */
    public val clientRecordId: String?

    /** **Long?** your own version of [clientRecordId]; Health Connect keeps the highest version */
    public val clientRecordVersion: Long?

    /** **Int?** zone offset at [startTimestamp] in seconds; null means unknown */
    public val startZoneOffsetSeconds: Int?

    /** **Int?** zone offset at [endTimestamp] in seconds; null means unknown */
    public val endZoneOffsetSeconds: Int?

    /** **RecordingMethod?** how the record was recorded; [RecordingMethod.MANUAL_ENTRY] when written as null */
    public val recordingMethod: RecordingMethod?

    /** **Device?** the device that recorded the data */
    public val device: Device?

    /** **DataOrigin?** the app that wrote the record; set by Health Connect */
    public val dataOrigin: DataOrigin?

    /** **Long?** last modification time, epoch milliseconds; set by Health Connect */
    public val lastModifiedTimestamp: Long?

    /** **HealthType** the data type of this sample */
    public val healthType: HealthType
}

/** **WritableSample** a sample type apps may insert, update and delete with **HealthConnectWriter** */
@Serializable
public sealed interface WritableSample : Sample
