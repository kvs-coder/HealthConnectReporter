package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.SleepSessionRecord
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asSleepStageType
import com.kvs.healthconnectreporter.decorator.asZoneOffset
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.decorator.encoded
import com.kvs.healthconnectreporter.decorator.epochMillis
import com.kvs.healthconnectreporter.decorator.lastModifiedTimestamp
import com.kvs.healthconnectreporter.decorator.metadata
import com.kvs.healthconnectreporter.decorator.sampleClientRecordVersion
import com.kvs.healthconnectreporter.decorator.sampleDataOrigin
import com.kvs.healthconnectreporter.decorator.sampleDevice
import com.kvs.healthconnectreporter.decorator.sampleId
import com.kvs.healthconnectreporter.decorator.sampleRecordingMethod
import com.kvs.healthconnectreporter.decorator.seconds
import com.kvs.healthconnectreporter.decorator.validateInside
import com.kvs.healthconnectreporter.decorator.validateInterval
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Payload
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import com.kvs.healthconnectreporter.model.type.SleepStageType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **SleepSession** a sleep session with its stages.
 *
 * @param startTimestamp **Long** epoch milliseconds
 * @param endTimestamp **Long** epoch milliseconds, not before [startTimestamp]
 * @param harmonized **Harmonized** the measured values
 * @param id **String?** Health Connect record id; null until inserted
 * @param clientRecordId **String?** your own id for the record
 * @param clientRecordVersion **Long?** your own version of [clientRecordId]
 * @param startZoneOffsetSeconds **Int?** zone offset at the start in seconds
 * @param endZoneOffsetSeconds **Int?** zone offset at the end in seconds
 * @param recordingMethod **RecordingMethod?** how the data was recorded; manual entry when null
 * @param device **Device?** the recording device
 * @param dataOrigin **DataOrigin?** the writing app; set by Health Connect
 * @param lastModifiedTimestamp **Long?** last modification, epoch milliseconds; set by Health Connect
 * @throws com.kvs.healthconnectreporter.HealthConnectException.InvalidValue when [endTimestamp] is before
 * [startTimestamp] or a stage lies outside the interval or ends before it starts
 */
@Serializable
@SerialName("sleep")
public data class SleepSession(
    override val startTimestamp: Long,
    override val endTimestamp: Long,
    val harmonized: Harmonized,
    override val id: String? = null,
    override val clientRecordId: String? = null,
    override val clientRecordVersion: Long? = null,
    override val startZoneOffsetSeconds: Int? = null,
    override val endZoneOffsetSeconds: Int? = null,
    override val recordingMethod: RecordingMethod? = null,
    override val device: Device? = null,
    override val dataOrigin: DataOrigin? = null,
    override val lastModifiedTimestamp: Long? = null,
) : WritableSample {
    init {
        validateInterval()
        harmonized.stages.forEach { validateInside(endTimestamp, it.startTimestamp, it.endTimestamp) }
    }

    override val healthType: HealthType get() = HealthType.SLEEP_SESSION

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of SleepSession.
     *
     * @param title **String?** the title of the session
     * @param notes **String?** notes on the session
     * @param stages **List<Stage>** the sleep stages, inside the session and not overlapping. Empty by default
     */
    @Serializable
    public data class Harmonized(
        val title: String? = null,
        val notes: String? = null,
        val stages: List<Stage> = emptyList(),
    )

    /**
     * **Stage** one sleep stage.
     *
     * @param startTimestamp **Long** epoch milliseconds
     * @param endTimestamp **Long** epoch milliseconds, after [startTimestamp]
     * @param stage **SleepStageType** the stage
     * @throws com.kvs.healthconnectreporter.HealthConnectException.InvalidValue when [endTimestamp] is before
     * [startTimestamp]
     */
    @Serializable
    public data class Stage(
        val startTimestamp: Long,
        val endTimestamp: Long,
        val stage: SleepStageType,
    ) {
        init {
            startTimestamp.validateInterval(endTimestamp)
        }
    }

    // region Original
    internal fun asOriginal(): SleepSessionRecord =
        SleepSessionRecord(
            startTime = startTimestamp.asInstant,
            startZoneOffset = startZoneOffsetSeconds.asZoneOffset,
            endTime = endTimestamp.asInstant,
            endZoneOffset = endZoneOffsetSeconds.asZoneOffset,
            metadata = metadata(),
            title = harmonized.title,
            notes = harmonized.notes,
            stages =
                harmonized.stages.map {
                    SleepSessionRecord.Stage(
                        startTime = it.startTimestamp.asInstant,
                        endTime = it.endTimestamp.asInstant,
                        stage = it.stage.constant,
                    )
                },
        )
    // endregion

    /** Factory of **SleepSession** */
    public companion object : Payload.Factory<SleepSession>({ SleepSession.serializer() }) {
        internal fun from(record: SleepSessionRecord): SleepSession =
            SleepSession(
                startTimestamp = record.startTime.epochMillis,
                endTimestamp = record.endTime.epochMillis,
                harmonized =
                    Harmonized(
                        title = record.title,
                        notes = record.notes,
                        stages =
                            record.stages.map {
                                Stage(
                                    startTimestamp = it.startTime.epochMillis,
                                    endTimestamp = it.endTime.epochMillis,
                                    stage = it.stage.asSleepStageType,
                                )
                            },
                    ),
                id = record.metadata.sampleId,
                clientRecordId = record.metadata.clientRecordId,
                clientRecordVersion = record.metadata.sampleClientRecordVersion,
                startZoneOffsetSeconds = record.startZoneOffset.seconds,
                endZoneOffsetSeconds = record.endZoneOffset.seconds,
                recordingMethod = record.metadata.sampleRecordingMethod,
                device = record.metadata.sampleDevice,
                dataOrigin = record.metadata.sampleDataOrigin,
                lastModifiedTimestamp = record.metadata.lastModifiedTimestamp,
            )
    }
}
