package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.SkinTemperatureRecord
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asSkinTemperatureMeasurementLocation
import com.kvs.healthconnectreporter.decorator.asTemperature
import com.kvs.healthconnectreporter.decorator.asTemperatureDelta
import com.kvs.healthconnectreporter.decorator.asZoneOffset
import com.kvs.healthconnectreporter.decorator.celsius
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
import com.kvs.healthconnectreporter.model.type.SkinTemperatureMeasurementLocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **SkinTemperature** skin temperature as deltas from a baseline, in °C.
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
 * [startTimestamp] or a delta lies outside the interval
 */
@Serializable
@SerialName("skin_temperature")
public data class SkinTemperature(
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
        harmonized.deltas.forEach { validateInside(endTimestamp, it.timestamp) }
    }

    override val healthType: HealthType get() = HealthType.SKIN_TEMPERATURE

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of SkinTemperature.
     *
     * @param deltas **List<Delta>** the measured deltas, inside the sample's interval
     * @param baseline **Double?** the baseline skin temperature in °C (0–100)
     * @param measurementLocation **SkinTemperatureMeasurementLocation** where the temperature was measured.
     * [SkinTemperatureMeasurementLocation.UNKNOWN] by default
     */
    @Serializable
    public data class Harmonized(
        val deltas: List<Delta>,
        val baseline: Double? = null,
        val measurementLocation: SkinTemperatureMeasurementLocation = SkinTemperatureMeasurementLocation.UNKNOWN,
    )

    /**
     * **Delta** one skin temperature measurement relative to the baseline.
     *
     * @param timestamp **Long** measurement time, epoch milliseconds
     * @param delta **Double** difference from the baseline in °C (-30–30)
     */
    @Serializable
    public data class Delta(
        val timestamp: Long,
        val delta: Double,
    )

    // region Original
    internal fun asOriginal(): SkinTemperatureRecord =
        SkinTemperatureRecord(
            startTime = startTimestamp.asInstant,
            startZoneOffset = startZoneOffsetSeconds.asZoneOffset,
            endTime = endTimestamp.asInstant,
            endZoneOffset = endZoneOffsetSeconds.asZoneOffset,
            metadata = metadata(),
            deltas =
                harmonized.deltas.map {
                    SkinTemperatureRecord.Delta(time = it.timestamp.asInstant, delta = it.delta.asTemperatureDelta)
                },
            baseline = harmonized.baseline?.asTemperature,
            measurementLocation = harmonized.measurementLocation.constant,
        )
    // endregion

    /** Factory of **SkinTemperature** */
    public companion object : Payload.Factory<SkinTemperature>({ SkinTemperature.serializer() }) {
        internal fun from(record: SkinTemperatureRecord): SkinTemperature =
            SkinTemperature(
                startTimestamp = record.startTime.epochMillis,
                endTimestamp = record.endTime.epochMillis,
                harmonized =
                    Harmonized(
                        deltas =
                            record.deltas.map {
                                Delta(timestamp = it.time.epochMillis, delta = it.delta.celsius)
                            },
                        baseline = record.baseline?.celsius,
                        measurementLocation = record.measurementLocation.asSkinTemperatureMeasurementLocation,
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
