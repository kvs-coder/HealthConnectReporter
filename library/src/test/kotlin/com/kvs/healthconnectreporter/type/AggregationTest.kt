package com.kvs.healthconnectreporter.type

import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Mass
import androidx.health.connect.client.units.Power
import androidx.health.connect.client.units.Pressure
import androidx.health.connect.client.units.TemperatureDelta
import androidx.health.connect.client.units.Velocity
import androidx.health.connect.client.units.Volume
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.asPeriod
import com.kvs.healthconnectreporter.decorator.canonical
import com.kvs.healthconnectreporter.decorator.metric
import com.kvs.healthconnectreporter.model.TimeGroup
import com.kvs.healthconnectreporter.model.type.Aggregation
import com.kvs.healthconnectreporter.model.type.TimeGroupUnit
import org.junit.Test
import java.time.Duration
import java.time.Period
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AggregationTest {
    @Test
    fun `every aggregation has its own metric`() {
        assertEquals(
            Aggregation.entries.size,
            Aggregation.entries
                .map { it.metric }
                .toSet()
                .size,
        )
    }

    @Test
    fun `aggregated values convert to canonical units`() {
        val expected =
            mapOf<Any, Double>(
                12L to 12.0,
                1.5 to 1.5,
                Energy.kilojoules(4.184) to 1.0,
                Length.kilometers(1.0) to 1000.0,
                Mass.grams(500.0) to 0.5,
                Power.watts(200.0) to 200.0,
                Pressure.millimetersOfMercury(120.0) to 120.0,
                Velocity.kilometersPerHour(36.0) to 10.0,
                Volume.milliliters(250.0) to 0.25,
                TemperatureDelta.celsius(0.5) to 0.5,
                Duration.ofMinutes(1) to 60_000.0,
            )

        expected.forEach { (value, canonical) -> assertEquals(canonical, value.canonical, 1e-9, value.toString()) }
    }

    @Test
    fun `unsupported aggregated value is an invalid value`() {
        assertFailsWith<HealthConnectException.InvalidValue> { "text".canonical }
    }

    @Test
    fun `periods group by their calendar unit`() {
        val expected =
            mapOf(
                TimeGroupUnit.DAILY to Period.ofDays(2),
                TimeGroupUnit.WEEKLY to Period.ofWeeks(2),
                TimeGroupUnit.MONTHLY to Period.ofMonths(2),
                TimeGroupUnit.YEARLY to Period.ofYears(2),
            )

        expected.forEach { (unit, period) -> assertEquals(period, TimeGroup.Period(unit, 2, "UTC").asPeriod) }
    }
}
