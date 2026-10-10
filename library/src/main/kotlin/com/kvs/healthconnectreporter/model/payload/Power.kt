package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.PowerRecord
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asPower
import com.kvs.healthconnectreporter.decorator.asZoneOffset
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
import com.kvs.healthconnectreporter.decorator.watts
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Payload
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **Power** power over an interval, in W.
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
 * [startTimestamp] or a series value lies outside the interval
 */
@Serializable
@SerialName("power")
public data class Power(
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
        harmonized.series.forEach { validateInside(endTimestamp, it.timestamp) }
    }

    override val healthType: HealthType get() = HealthType.POWER

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of Power.
     *
     * @param series **List<Series>** the measurements, inside the sample's interval
     */
    @Serializable
    public data class Harmonized(
        val series: List<Series>,
    )

    /**
     * **Series** one measurement.
     *
     * @param timestamp **Long** measurement time, epoch milliseconds
     * @param power **Double** power in W (0–100 000)
     */
    @Serializable
    public data class Series(
        val timestamp: Long,
        val power: Double,
    )

    // region Original
    internal fun asOriginal(): PowerRecord =
        PowerRecord(
            startTime = startTimestamp.asInstant,
            startZoneOffset = startZoneOffsetSeconds.asZoneOffset,
            endTime = endTimestamp.asInstant,
            endZoneOffset = endZoneOffsetSeconds.asZoneOffset,
            samples =
                harmonized.series.map {
                    PowerRecord.Sample(time = it.timestamp.asInstant, power = it.power.asPower)
                },
            metadata = metadata(),
        )
    // endregion

    /** Factory of **Power** */
    public companion object : Payload.Factory<Power>({ Power.serializer() }) {
        internal fun from(record: PowerRecord): Power =
            Power(
                startTimestamp = record.startTime.epochMillis,
                endTimestamp = record.endTime.epochMillis,
                harmonized =
                    Harmonized(
                        series =
                            record.samples.map {
                                Series(timestamp = it.time.epochMillis, power = it.power.watts)
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
