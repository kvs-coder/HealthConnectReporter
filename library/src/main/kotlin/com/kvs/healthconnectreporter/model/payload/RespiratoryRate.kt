package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.RespiratoryRateRecord
import com.kvs.healthconnectreporter.decorator.asInstant
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
import com.kvs.healthconnectreporter.decorator.validateInterval
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Payload
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **RespiratoryRate** respiratory rate, in breaths per minute.
 *
 * @param startTimestamp **Long** measurement time, epoch milliseconds
 * @param endTimestamp **Long?** not used: the record is a single measurement at [startTimestamp]
 * @param harmonized **Harmonized** the measured values
 * @param id **String?** Health Connect record id; null until inserted
 * @param clientRecordId **String?** your own id for the record
 * @param clientRecordVersion **Long?** your own version of [clientRecordId]
 * @param startZoneOffsetSeconds **Int?** zone offset in seconds
 * @param endZoneOffsetSeconds **Int?** not used
 * @param recordingMethod **RecordingMethod?** how the data was recorded; manual entry when null
 * @param device **Device?** the recording device
 * @param dataOrigin **DataOrigin?** the writing app; set by Health Connect
 * @param lastModifiedTimestamp **Long?** last modification, epoch milliseconds; set by Health Connect
 * @throws com.kvs.healthconnectreporter.HealthConnectException.InvalidValue when [endTimestamp] is before
 * [startTimestamp]
 */
@Serializable
@SerialName("respiratory_rate")
public data class RespiratoryRate(
    override val startTimestamp: Long,
    override val endTimestamp: Long? = null,
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
    }

    override val healthType: HealthType get() = HealthType.RESPIRATORY_RATE

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of RespiratoryRate.
     *
     * @param rate **Double** respiratory rate in breaths per minute (0–1000)
     */
    @Serializable
    public data class Harmonized(
        val rate: Double,
    )

    // region Original
    internal fun asOriginal(): RespiratoryRateRecord =
        RespiratoryRateRecord(
            time = startTimestamp.asInstant,
            zoneOffset = startZoneOffsetSeconds.asZoneOffset,
            rate = harmonized.rate,
            metadata = metadata(),
        )
    // endregion

    /** Factory of **RespiratoryRate** */
    public companion object : Payload.Factory<RespiratoryRate>({ RespiratoryRate.serializer() }) {
        internal fun from(record: RespiratoryRateRecord): RespiratoryRate =
            RespiratoryRate(
                startTimestamp = record.time.epochMillis,
                harmonized =
                    Harmonized(
                        rate = record.rate,
                    ),
                id = record.metadata.sampleId,
                clientRecordId = record.metadata.clientRecordId,
                clientRecordVersion = record.metadata.sampleClientRecordVersion,
                startZoneOffsetSeconds = record.zoneOffset.seconds,
                recordingMethod = record.metadata.sampleRecordingMethod,
                device = record.metadata.sampleDevice,
                dataOrigin = record.metadata.sampleDataOrigin,
                lastModifiedTimestamp = record.metadata.lastModifiedTimestamp,
            )
    }
}
