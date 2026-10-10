package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.Vo2MaxRecord
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asVo2MaxMeasurementMethod
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
import com.kvs.healthconnectreporter.decorator.validateInterval
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Payload
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import com.kvs.healthconnectreporter.model.type.Vo2MaxMeasurementMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **Vo2Max** maximal oxygen uptake (VO2 max), in mL/(min·kg).
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
@SerialName("vo2_max")
public data class Vo2Max(
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

    override val healthType: HealthType get() = HealthType.VO2_MAX

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of Vo2Max.
     *
     * @param vo2MillilitersPerMinuteKilogram **Double** VO2 max in mL/(min·kg) (0–100)
     * @param measurementMethod **Vo2MaxMeasurementMethod** how VO2 max was measured.
     * [Vo2MaxMeasurementMethod.OTHER] by default
     */
    @Serializable
    public data class Harmonized(
        val vo2MillilitersPerMinuteKilogram: Double,
        val measurementMethod: Vo2MaxMeasurementMethod = Vo2MaxMeasurementMethod.OTHER,
    )

    // region Original
    internal fun asOriginal(): Vo2MaxRecord =
        Vo2MaxRecord(
            time = startTimestamp.asInstant,
            zoneOffset = startZoneOffsetSeconds.asZoneOffset,
            vo2MillilitersPerMinuteKilogram = harmonized.vo2MillilitersPerMinuteKilogram,
            measurementMethod = harmonized.measurementMethod.constant,
            metadata = metadata(),
        )
    // endregion

    /** Factory of **Vo2Max** */
    public companion object : Payload.Factory<Vo2Max>({ Vo2Max.serializer() }) {
        internal fun from(record: Vo2MaxRecord): Vo2Max =
            Vo2Max(
                startTimestamp = record.time.epochMillis,
                harmonized =
                    Harmonized(
                        vo2MillilitersPerMinuteKilogram = record.vo2MillilitersPerMinuteKilogram,
                        measurementMethod = record.measurementMethod.asVo2MaxMeasurementMethod,
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
