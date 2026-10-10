package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.NutritionRecord
import com.kvs.healthconnectreporter.decorator.asEnergy
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asMass
import com.kvs.healthconnectreporter.decorator.asMealType
import com.kvs.healthconnectreporter.decorator.asZoneOffset
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.decorator.encoded
import com.kvs.healthconnectreporter.decorator.epochMillis
import com.kvs.healthconnectreporter.decorator.kilocalories
import com.kvs.healthconnectreporter.decorator.kilograms
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
import com.kvs.healthconnectreporter.model.type.MealType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **Nutrition** nutrients of a meal or food; masses in kg, energy in kcal.
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
 * [startTimestamp]
 */
@Serializable
@SerialName("nutrition")
public data class Nutrition(
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
    }

    override val healthType: HealthType get() = HealthType.NUTRITION

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of Nutrition.
     *
     * @param biotin **Double?** biotin in kg
     * @param caffeine **Double?** caffeine in kg
     * @param calcium **Double?** calcium in kg
     * @param energy **Double?** energy in kcal
     * @param energyFromFat **Double?** energy from fat in kcal
     * @param chloride **Double?** chloride in kg
     * @param cholesterol **Double?** cholesterol in kg
     * @param chromium **Double?** chromium in kg
     * @param copper **Double?** copper in kg
     * @param dietaryFiber **Double?** dietary fiber in kg
     * @param folate **Double?** folate in kg
     * @param folicAcid **Double?** folic acid in kg
     * @param iodine **Double?** iodine in kg
     * @param iron **Double?** iron in kg
     * @param magnesium **Double?** magnesium in kg
     * @param manganese **Double?** manganese in kg
     * @param molybdenum **Double?** molybdenum in kg
     * @param monounsaturatedFat **Double?** monounsaturated fat in kg
     * @param niacin **Double?** niacin in kg
     * @param pantothenicAcid **Double?** pantothenic acid in kg
     * @param phosphorus **Double?** phosphorus in kg
     * @param polyunsaturatedFat **Double?** polyunsaturated fat in kg
     * @param potassium **Double?** potassium in kg
     * @param protein **Double?** protein in kg
     * @param riboflavin **Double?** riboflavin in kg
     * @param saturatedFat **Double?** saturated fat in kg
     * @param selenium **Double?** selenium in kg
     * @param sodium **Double?** sodium in kg
     * @param sugar **Double?** sugar in kg
     * @param thiamin **Double?** thiamin in kg
     * @param totalCarbohydrate **Double?** total carbohydrate in kg
     * @param totalFat **Double?** total fat in kg
     * @param transFat **Double?** trans fat in kg
     * @param unsaturatedFat **Double?** unsaturated fat in kg
     * @param vitaminA **Double?** vitamin a in kg
     * @param vitaminB12 **Double?** vitamin B12 in kg
     * @param vitaminB6 **Double?** vitamin B6 in kg
     * @param vitaminC **Double?** vitamin c in kg
     * @param vitaminD **Double?** vitamin d in kg
     * @param vitaminE **Double?** vitamin e in kg
     * @param vitaminK **Double?** vitamin k in kg
     * @param zinc **Double?** zinc in kg
     * @param name **String?** the name of the food or meal
     * @param mealType **MealType** the meal. [MealType.UNKNOWN] by default
     */
    @Serializable
    public data class Harmonized(
        val biotin: Double? = null,
        val caffeine: Double? = null,
        val calcium: Double? = null,
        val energy: Double? = null,
        val energyFromFat: Double? = null,
        val chloride: Double? = null,
        val cholesterol: Double? = null,
        val chromium: Double? = null,
        val copper: Double? = null,
        val dietaryFiber: Double? = null,
        val folate: Double? = null,
        val folicAcid: Double? = null,
        val iodine: Double? = null,
        val iron: Double? = null,
        val magnesium: Double? = null,
        val manganese: Double? = null,
        val molybdenum: Double? = null,
        val monounsaturatedFat: Double? = null,
        val niacin: Double? = null,
        val pantothenicAcid: Double? = null,
        val phosphorus: Double? = null,
        val polyunsaturatedFat: Double? = null,
        val potassium: Double? = null,
        val protein: Double? = null,
        val riboflavin: Double? = null,
        val saturatedFat: Double? = null,
        val selenium: Double? = null,
        val sodium: Double? = null,
        val sugar: Double? = null,
        val thiamin: Double? = null,
        val totalCarbohydrate: Double? = null,
        val totalFat: Double? = null,
        val transFat: Double? = null,
        val unsaturatedFat: Double? = null,
        val vitaminA: Double? = null,
        val vitaminB12: Double? = null,
        val vitaminB6: Double? = null,
        val vitaminC: Double? = null,
        val vitaminD: Double? = null,
        val vitaminE: Double? = null,
        val vitaminK: Double? = null,
        val zinc: Double? = null,
        val name: String? = null,
        val mealType: MealType = MealType.UNKNOWN,
    )

    // region Original
    internal fun asOriginal(): NutritionRecord =
        NutritionRecord(
            startTime = startTimestamp.asInstant,
            startZoneOffset = startZoneOffsetSeconds.asZoneOffset,
            endTime = endTimestamp.asInstant,
            endZoneOffset = endZoneOffsetSeconds.asZoneOffset,
            biotin = harmonized.biotin?.asMass,
            caffeine = harmonized.caffeine?.asMass,
            calcium = harmonized.calcium?.asMass,
            energy = harmonized.energy?.asEnergy,
            energyFromFat = harmonized.energyFromFat?.asEnergy,
            chloride = harmonized.chloride?.asMass,
            cholesterol = harmonized.cholesterol?.asMass,
            chromium = harmonized.chromium?.asMass,
            copper = harmonized.copper?.asMass,
            dietaryFiber = harmonized.dietaryFiber?.asMass,
            folate = harmonized.folate?.asMass,
            folicAcid = harmonized.folicAcid?.asMass,
            iodine = harmonized.iodine?.asMass,
            iron = harmonized.iron?.asMass,
            magnesium = harmonized.magnesium?.asMass,
            manganese = harmonized.manganese?.asMass,
            molybdenum = harmonized.molybdenum?.asMass,
            monounsaturatedFat = harmonized.monounsaturatedFat?.asMass,
            niacin = harmonized.niacin?.asMass,
            pantothenicAcid = harmonized.pantothenicAcid?.asMass,
            phosphorus = harmonized.phosphorus?.asMass,
            polyunsaturatedFat = harmonized.polyunsaturatedFat?.asMass,
            potassium = harmonized.potassium?.asMass,
            protein = harmonized.protein?.asMass,
            riboflavin = harmonized.riboflavin?.asMass,
            saturatedFat = harmonized.saturatedFat?.asMass,
            selenium = harmonized.selenium?.asMass,
            sodium = harmonized.sodium?.asMass,
            sugar = harmonized.sugar?.asMass,
            thiamin = harmonized.thiamin?.asMass,
            totalCarbohydrate = harmonized.totalCarbohydrate?.asMass,
            totalFat = harmonized.totalFat?.asMass,
            transFat = harmonized.transFat?.asMass,
            unsaturatedFat = harmonized.unsaturatedFat?.asMass,
            vitaminA = harmonized.vitaminA?.asMass,
            vitaminB12 = harmonized.vitaminB12?.asMass,
            vitaminB6 = harmonized.vitaminB6?.asMass,
            vitaminC = harmonized.vitaminC?.asMass,
            vitaminD = harmonized.vitaminD?.asMass,
            vitaminE = harmonized.vitaminE?.asMass,
            vitaminK = harmonized.vitaminK?.asMass,
            zinc = harmonized.zinc?.asMass,
            name = harmonized.name,
            mealType = harmonized.mealType.constant,
            metadata = metadata(),
        )
    // endregion

    /** Factory of **Nutrition** */
    public companion object : Payload.Factory<Nutrition>({ Nutrition.serializer() }) {
        internal fun from(record: NutritionRecord): Nutrition =
            Nutrition(
                startTimestamp = record.startTime.epochMillis,
                endTimestamp = record.endTime.epochMillis,
                harmonized =
                    Harmonized(
                        biotin = record.biotin?.kilograms,
                        caffeine = record.caffeine?.kilograms,
                        calcium = record.calcium?.kilograms,
                        energy = record.energy?.kilocalories,
                        energyFromFat = record.energyFromFat?.kilocalories,
                        chloride = record.chloride?.kilograms,
                        cholesterol = record.cholesterol?.kilograms,
                        chromium = record.chromium?.kilograms,
                        copper = record.copper?.kilograms,
                        dietaryFiber = record.dietaryFiber?.kilograms,
                        folate = record.folate?.kilograms,
                        folicAcid = record.folicAcid?.kilograms,
                        iodine = record.iodine?.kilograms,
                        iron = record.iron?.kilograms,
                        magnesium = record.magnesium?.kilograms,
                        manganese = record.manganese?.kilograms,
                        molybdenum = record.molybdenum?.kilograms,
                        monounsaturatedFat = record.monounsaturatedFat?.kilograms,
                        niacin = record.niacin?.kilograms,
                        pantothenicAcid = record.pantothenicAcid?.kilograms,
                        phosphorus = record.phosphorus?.kilograms,
                        polyunsaturatedFat = record.polyunsaturatedFat?.kilograms,
                        potassium = record.potassium?.kilograms,
                        protein = record.protein?.kilograms,
                        riboflavin = record.riboflavin?.kilograms,
                        saturatedFat = record.saturatedFat?.kilograms,
                        selenium = record.selenium?.kilograms,
                        sodium = record.sodium?.kilograms,
                        sugar = record.sugar?.kilograms,
                        thiamin = record.thiamin?.kilograms,
                        totalCarbohydrate = record.totalCarbohydrate?.kilograms,
                        totalFat = record.totalFat?.kilograms,
                        transFat = record.transFat?.kilograms,
                        unsaturatedFat = record.unsaturatedFat?.kilograms,
                        vitaminA = record.vitaminA?.kilograms,
                        vitaminB12 = record.vitaminB12?.kilograms,
                        vitaminB6 = record.vitaminB6?.kilograms,
                        vitaminC = record.vitaminC?.kilograms,
                        vitaminD = record.vitaminD?.kilograms,
                        vitaminE = record.vitaminE?.kilograms,
                        vitaminK = record.vitaminK?.kilograms,
                        zinc = record.zinc?.kilograms,
                        name = record.name,
                        mealType = record.mealType.asMealType,
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
