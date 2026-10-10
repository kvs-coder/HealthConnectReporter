package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.BloodPressureRecord
import com.kvs.healthconnectreporter.decorator.asBloodPressureMeasurementLocation
import com.kvs.healthconnectreporter.decorator.asBodyPosition
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asPressure
import com.kvs.healthconnectreporter.decorator.asZoneOffset
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.decorator.encoded
import com.kvs.healthconnectreporter.decorator.epochMillis
import com.kvs.healthconnectreporter.decorator.lastModifiedTimestamp
import com.kvs.healthconnectreporter.decorator.metadata
import com.kvs.healthconnectreporter.decorator.millimetersOfMercury
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
import com.kvs.healthconnectreporter.model.type.BloodPressureMeasurementLocation
import com.kvs.healthconnectreporter.model.type.BodyPosition
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **BloodPressure** a blood pressure measurement, in mmHg.
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
@SerialName("blood_pressure")
public data class BloodPressure(
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

    override val healthType: HealthType get() = HealthType.BLOOD_PRESSURE

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of BloodPressure.
     *
     * @param systolic **Double** systolic pressure in mmHg (20–200)
     * @param diastolic **Double** diastolic pressure in mmHg (10–180)
     * @param bodyPosition **BodyPosition** the body position during the measurement. [BodyPosition.UNKNOWN] by default
     * @param measurementLocation **BloodPressureMeasurementLocation** the arm and part of the arm measured.
     * [BloodPressureMeasurementLocation.UNKNOWN] by default
     */
    @Serializable
    public data class Harmonized(
        val systolic: Double,
        val diastolic: Double,
        val bodyPosition: BodyPosition = BodyPosition.UNKNOWN,
        val measurementLocation: BloodPressureMeasurementLocation = BloodPressureMeasurementLocation.UNKNOWN,
    )

    // region Original
    internal fun asOriginal(): BloodPressureRecord =
        BloodPressureRecord(
            time = startTimestamp.asInstant,
            zoneOffset = startZoneOffsetSeconds.asZoneOffset,
            systolic = harmonized.systolic.asPressure,
            diastolic = harmonized.diastolic.asPressure,
            bodyPosition = harmonized.bodyPosition.constant,
            measurementLocation = harmonized.measurementLocation.constant,
            metadata = metadata(),
        )
    // endregion

    /** Factory of **BloodPressure** */
    public companion object : Payload.Factory<BloodPressure>({ BloodPressure.serializer() }) {
        internal fun from(record: BloodPressureRecord): BloodPressure =
            BloodPressure(
                startTimestamp = record.time.epochMillis,
                harmonized =
                    Harmonized(
                        systolic = record.systolic.millimetersOfMercury,
                        diastolic = record.diastolic.millimetersOfMercury,
                        bodyPosition = record.bodyPosition.asBodyPosition,
                        measurementLocation = record.measurementLocation.asBloodPressureMeasurementLocation,
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
