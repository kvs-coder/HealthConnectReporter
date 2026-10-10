package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.units.BloodGlucose
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Mass
import androidx.health.connect.client.units.Percentage
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.Pressure
import androidx.health.connect.client.units.Temperature
import androidx.health.connect.client.units.TemperatureDelta
import androidx.health.connect.client.units.Velocity
import androidx.health.connect.client.units.Volume

// The only place that converts between Health Connect units and the canonical payload units (ADR 0004).

internal val Double.asMass: Mass get() = Mass.kilograms(this)
internal val Mass.kilograms: Double get() = inKilograms

internal val Double.asEnergy: Energy get() = Energy.kilocalories(this)
internal val Energy.kilocalories: Double get() = inKilocalories

internal val Double.asLength: Length get() = Length.meters(this)
internal val Length.meters: Double get() = inMeters

internal val Double.asVelocity: Velocity get() = Velocity.metersPerSecond(this)
internal val Velocity.metersPerSecond: Double get() = inMetersPerSecond

internal val Double.asPower: Power get() = Power.watts(this)
internal val Power.watts: Double get() = inWatts

internal val Double.asPressure: Pressure get() = Pressure.millimetersOfMercury(this)
internal val Pressure.millimetersOfMercury: Double get() = inMillimetersOfMercury

internal val Double.asTemperature: Temperature get() = Temperature.celsius(this)
internal val Temperature.celsius: Double get() = inCelsius

internal val Double.asTemperatureDelta: TemperatureDelta get() = TemperatureDelta.celsius(this)
internal val TemperatureDelta.celsius: Double get() = inCelsius

internal val Double.asVolume: Volume get() = Volume.liters(this)
internal val Volume.liters: Double get() = inLiters

internal val Double.asBloodGlucose: BloodGlucose get() = BloodGlucose.millimolesPerLiter(this)
internal val BloodGlucose.millimolesPerLiter: Double get() = inMillimolesPerLiter

internal val Double.asPercentage: Percentage get() = Percentage(this)
internal val Percentage.percent: Double get() = value
