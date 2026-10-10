@file:OptIn(ExperimentalMindfulnessSessionApi::class)

package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.feature.ExperimentalMindfulnessSessionApi
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.metadata.Metadata
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.model.payload.ActiveCaloriesBurned
import com.kvs.healthconnectreporter.model.payload.BasalBodyTemperature
import com.kvs.healthconnectreporter.model.payload.BasalMetabolicRate
import com.kvs.healthconnectreporter.model.payload.BloodGlucose
import com.kvs.healthconnectreporter.model.payload.BloodPressure
import com.kvs.healthconnectreporter.model.payload.BodyFat
import com.kvs.healthconnectreporter.model.payload.BodyTemperature
import com.kvs.healthconnectreporter.model.payload.BodyWaterMass
import com.kvs.healthconnectreporter.model.payload.BoneMass
import com.kvs.healthconnectreporter.model.payload.CervicalMucus
import com.kvs.healthconnectreporter.model.payload.CyclingPedalingCadence
import com.kvs.healthconnectreporter.model.payload.Distance
import com.kvs.healthconnectreporter.model.payload.ElevationGained
import com.kvs.healthconnectreporter.model.payload.ExerciseSession
import com.kvs.healthconnectreporter.model.payload.FloorsClimbed
import com.kvs.healthconnectreporter.model.payload.HeartRate
import com.kvs.healthconnectreporter.model.payload.HeartRateVariabilityRmssd
import com.kvs.healthconnectreporter.model.payload.Height
import com.kvs.healthconnectreporter.model.payload.Hydration
import com.kvs.healthconnectreporter.model.payload.IntermenstrualBleeding
import com.kvs.healthconnectreporter.model.payload.LeanBodyMass
import com.kvs.healthconnectreporter.model.payload.MenstruationFlow
import com.kvs.healthconnectreporter.model.payload.MenstruationPeriod
import com.kvs.healthconnectreporter.model.payload.MindfulnessSession
import com.kvs.healthconnectreporter.model.payload.Nutrition
import com.kvs.healthconnectreporter.model.payload.OvulationTest
import com.kvs.healthconnectreporter.model.payload.OxygenSaturation
import com.kvs.healthconnectreporter.model.payload.PlannedExerciseSession
import com.kvs.healthconnectreporter.model.payload.Power
import com.kvs.healthconnectreporter.model.payload.RespiratoryRate
import com.kvs.healthconnectreporter.model.payload.RestingHeartRate
import com.kvs.healthconnectreporter.model.payload.SexualActivity
import com.kvs.healthconnectreporter.model.payload.SkinTemperature
import com.kvs.healthconnectreporter.model.payload.SleepSession
import com.kvs.healthconnectreporter.model.payload.Speed
import com.kvs.healthconnectreporter.model.payload.Steps
import com.kvs.healthconnectreporter.model.payload.StepsCadence
import com.kvs.healthconnectreporter.model.payload.TotalCaloriesBurned
import com.kvs.healthconnectreporter.model.payload.Vo2Max
import com.kvs.healthconnectreporter.model.payload.Weight
import com.kvs.healthconnectreporter.model.payload.WheelchairPushes
import com.kvs.healthconnectreporter.model.payload.WritableSample
import com.kvs.healthconnectreporter.model.type.RecordingMethod

internal val WritableSample.asOriginal: Record
    get() =
        when (this) {
            is ActiveCaloriesBurned -> asOriginal()
            is BasalBodyTemperature -> asOriginal()
            is BasalMetabolicRate -> asOriginal()
            is BloodGlucose -> asOriginal()
            is BloodPressure -> asOriginal()
            is BodyFat -> asOriginal()
            is BodyTemperature -> asOriginal()
            is BodyWaterMass -> asOriginal()
            is BoneMass -> asOriginal()
            is CervicalMucus -> asOriginal()
            is CyclingPedalingCadence -> asOriginal()
            is Distance -> asOriginal()
            is ElevationGained -> asOriginal()
            is ExerciseSession -> asOriginal()
            is FloorsClimbed -> asOriginal()
            is HeartRate -> asOriginal()
            is HeartRateVariabilityRmssd -> asOriginal()
            is Height -> asOriginal()
            is Hydration -> asOriginal()
            is IntermenstrualBleeding -> asOriginal()
            is LeanBodyMass -> asOriginal()
            is MenstruationFlow -> asOriginal()
            is MenstruationPeriod -> asOriginal()
            is MindfulnessSession -> asOriginal()
            is Nutrition -> asOriginal()
            is OvulationTest -> asOriginal()
            is OxygenSaturation -> asOriginal()
            is PlannedExerciseSession -> asOriginal()
            is Power -> asOriginal()
            is RespiratoryRate -> asOriginal()
            is RestingHeartRate -> asOriginal()
            is SexualActivity -> asOriginal()
            is SkinTemperature -> asOriginal()
            is SleepSession -> asOriginal()
            is Speed -> asOriginal()
            is Steps -> asOriginal()
            is StepsCadence -> asOriginal()
            is TotalCaloriesBurned -> asOriginal()
            is Vo2Max -> asOriginal()
            is Weight -> asOriginal()
            is WheelchairPushes -> asOriginal()
        }

internal fun WritableSample.withClientRecordId(clientRecordId: String): WritableSample =
    when (this) {
        is ActiveCaloriesBurned -> copy(clientRecordId = clientRecordId)
        is BasalBodyTemperature -> copy(clientRecordId = clientRecordId)
        is BasalMetabolicRate -> copy(clientRecordId = clientRecordId)
        is BloodGlucose -> copy(clientRecordId = clientRecordId)
        is BloodPressure -> copy(clientRecordId = clientRecordId)
        is BodyFat -> copy(clientRecordId = clientRecordId)
        is BodyTemperature -> copy(clientRecordId = clientRecordId)
        is BodyWaterMass -> copy(clientRecordId = clientRecordId)
        is BoneMass -> copy(clientRecordId = clientRecordId)
        is CervicalMucus -> copy(clientRecordId = clientRecordId)
        is CyclingPedalingCadence -> copy(clientRecordId = clientRecordId)
        is Distance -> copy(clientRecordId = clientRecordId)
        is ElevationGained -> copy(clientRecordId = clientRecordId)
        is ExerciseSession -> copy(clientRecordId = clientRecordId)
        is FloorsClimbed -> copy(clientRecordId = clientRecordId)
        is HeartRate -> copy(clientRecordId = clientRecordId)
        is HeartRateVariabilityRmssd -> copy(clientRecordId = clientRecordId)
        is Height -> copy(clientRecordId = clientRecordId)
        is Hydration -> copy(clientRecordId = clientRecordId)
        is IntermenstrualBleeding -> copy(clientRecordId = clientRecordId)
        is LeanBodyMass -> copy(clientRecordId = clientRecordId)
        is MenstruationFlow -> copy(clientRecordId = clientRecordId)
        is MenstruationPeriod -> copy(clientRecordId = clientRecordId)
        is MindfulnessSession -> copy(clientRecordId = clientRecordId)
        is Nutrition -> copy(clientRecordId = clientRecordId)
        is OvulationTest -> copy(clientRecordId = clientRecordId)
        is OxygenSaturation -> copy(clientRecordId = clientRecordId)
        is PlannedExerciseSession -> copy(clientRecordId = clientRecordId)
        is Power -> copy(clientRecordId = clientRecordId)
        is RespiratoryRate -> copy(clientRecordId = clientRecordId)
        is RestingHeartRate -> copy(clientRecordId = clientRecordId)
        is SexualActivity -> copy(clientRecordId = clientRecordId)
        is SkinTemperature -> copy(clientRecordId = clientRecordId)
        is SleepSession -> copy(clientRecordId = clientRecordId)
        is Speed -> copy(clientRecordId = clientRecordId)
        is Steps -> copy(clientRecordId = clientRecordId)
        is StepsCadence -> copy(clientRecordId = clientRecordId)
        is TotalCaloriesBurned -> copy(clientRecordId = clientRecordId)
        is Vo2Max -> copy(clientRecordId = clientRecordId)
        is Weight -> copy(clientRecordId = clientRecordId)
        is WheelchairPushes -> copy(clientRecordId = clientRecordId)
    }

/**
 * The record metadata: the `Metadata` factory for [WritableSample.recordingMethod] (manual entry by default),
 * with the stored [WritableSample.id] when there is one, else with [WritableSample.clientRecordId].
 *
 * @throws HealthConnectException.InvalidValue when an actively or automatically recorded sample has no device
 */
internal fun WritableSample.metadata(): Metadata {
    val id = id
    val clientRecordId = clientRecordId
    val version = clientRecordVersion ?: 0
    val device = device?.asOriginal
    return when (recordingMethod ?: RecordingMethod.MANUAL_ENTRY) {
        RecordingMethod.MANUAL_ENTRY ->
            when {
                id != null -> Metadata.manualEntryWithId(id, device)
                clientRecordId != null -> Metadata.manualEntry(clientRecordId, version, device)
                else -> Metadata.manualEntry(device)
            }
        RecordingMethod.UNKNOWN ->
            when {
                id != null -> Metadata.unknownRecordingMethodWithId(id, device)
                clientRecordId != null -> Metadata.unknownRecordingMethod(clientRecordId, version, device)
                else -> Metadata.unknownRecordingMethod(device)
            }
        RecordingMethod.ACTIVELY_RECORDED -> {
            val recorder = device ?: throw missingDevice()
            when {
                id != null -> Metadata.activelyRecordedWithId(id, recorder)
                clientRecordId != null -> Metadata.activelyRecorded(recorder, clientRecordId, version)
                else -> Metadata.activelyRecorded(recorder)
            }
        }
        RecordingMethod.AUTOMATICALLY_RECORDED -> {
            val recorder = device ?: throw missingDevice()
            when {
                id != null -> Metadata.autoRecordedWithId(id, recorder)
                clientRecordId != null -> Metadata.autoRecorded(recorder, clientRecordId, version)
                else -> Metadata.autoRecorded(recorder)
            }
        }
    }
}

private fun WritableSample.missingDevice() =
    HealthConnectException.InvalidValue("Invalid ${healthType.identifier}: $recordingMethod needs a device")
