package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.BloodGlucoseRecord
import com.kvs.healthconnectreporter.decorator.asBloodGlucose
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asMealType
import com.kvs.healthconnectreporter.decorator.asRelationToMeal
import com.kvs.healthconnectreporter.decorator.asSpecimenSource
import com.kvs.healthconnectreporter.decorator.asZoneOffset
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.decorator.encoded
import com.kvs.healthconnectreporter.decorator.epochMillis
import com.kvs.healthconnectreporter.decorator.lastModifiedTimestamp
import com.kvs.healthconnectreporter.decorator.metadata
import com.kvs.healthconnectreporter.decorator.millimolesPerLiter
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
import com.kvs.healthconnectreporter.model.type.MealType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import com.kvs.healthconnectreporter.model.type.RelationToMeal
import com.kvs.healthconnectreporter.model.type.SpecimenSource
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **BloodGlucose** a blood glucose measurement, in mmol/L.
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
@SerialName("blood_glucose")
public data class BloodGlucose(
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

    override val healthType: HealthType get() = HealthType.BLOOD_GLUCOSE

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of BloodGlucose.
     *
     * @param level **Double** blood glucose level in mmol/L (0–50)
     * @param specimenSource **SpecimenSource** the measured specimen. [SpecimenSource.UNKNOWN] by default
     * @param mealType **MealType** the meal the measurement relates to. [MealType.UNKNOWN] by default
     * @param relationToMeal **RelationToMeal** when the measurement was taken relative to the meal.
     * [RelationToMeal.UNKNOWN] by default
     */
    @Serializable
    public data class Harmonized(
        val level: Double,
        val specimenSource: SpecimenSource = SpecimenSource.UNKNOWN,
        val mealType: MealType = MealType.UNKNOWN,
        val relationToMeal: RelationToMeal = RelationToMeal.UNKNOWN,
    )

    // region Original
    internal fun asOriginal(): BloodGlucoseRecord =
        BloodGlucoseRecord(
            time = startTimestamp.asInstant,
            zoneOffset = startZoneOffsetSeconds.asZoneOffset,
            level = harmonized.level.asBloodGlucose,
            specimenSource = harmonized.specimenSource.constant,
            mealType = harmonized.mealType.constant,
            relationToMeal = harmonized.relationToMeal.constant,
            metadata = metadata(),
        )
    // endregion

    /** Factory of **BloodGlucose** */
    public companion object : Payload.Factory<BloodGlucose>({ BloodGlucose.serializer() }) {
        internal fun from(record: BloodGlucoseRecord): BloodGlucose =
            BloodGlucose(
                startTimestamp = record.time.epochMillis,
                harmonized =
                    Harmonized(
                        level = record.level.millimolesPerLiter,
                        specimenSource = record.specimenSource.asSpecimenSource,
                        mealType = record.mealType.asMealType,
                        relationToMeal = record.relationToMeal.asRelationToMeal,
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
