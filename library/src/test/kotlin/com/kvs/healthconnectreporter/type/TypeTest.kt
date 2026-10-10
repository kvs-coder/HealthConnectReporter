@file:OptIn(ExperimentalMindfulnessSessionApi::class)

package com.kvs.healthconnectreporter.type

import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.feature.ExperimentalMindfulnessSessionApi
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.decorator.readPermission
import com.kvs.healthconnectreporter.decorator.recordClass
import com.kvs.healthconnectreporter.decorator.writePermission
import com.kvs.healthconnectreporter.model.type.Aggregation
import com.kvs.healthconnectreporter.model.type.Feature
import com.kvs.healthconnectreporter.model.type.HealthType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TypeTest {
    @Test
    fun `make finds every type by its identifier`() {
        HealthType.entries.forEach { assertEquals(it, HealthType.make(it.identifier)) }
    }

    @Test
    fun `make with an unknown identifier is an invalid type`() {
        val error = assertFailsWith<HealthConnectException.InvalidType> { HealthType.make("unknown") }

        assertEquals("Invalid HealthType identifier: unknown", error.message)
    }

    @Test
    fun `identifiers and record classes are unique`() {
        assertEquals(
            HealthType.entries.size,
            HealthType.entries
                .map { it.identifier }
                .toSet()
                .size,
        )
        assertEquals(
            HealthType.entries.size,
            HealthType.entries
                .map { it.recordClass }
                .toSet()
                .size,
        )
    }

    @Test
    fun `identifiers match samsung health reporter where the concept matches`() {
        val expected =
            mapOf(
                HealthType.HEART_RATE to "heart_rate",
                HealthType.OXYGEN_SATURATION to "blood_oxygen",
                HealthType.BLOOD_PRESSURE to "blood_pressure",
                HealthType.BLOOD_GLUCOSE to "blood_glucose",
                HealthType.BODY_TEMPERATURE to "body_temperature",
                HealthType.SKIN_TEMPERATURE to "skin_temperature",
                HealthType.EXERCISE_SESSION to "exercise",
                HealthType.FLOORS_CLIMBED to "floors_climbed",
                HealthType.SLEEP_SESSION to "sleep",
                HealthType.NUTRITION to "nutrition",
                HealthType.HYDRATION to "water_intake",
                HealthType.STEPS to "steps",
            )

        expected.forEach { (type, identifier) -> assertEquals(identifier, type.identifier) }
    }

    @Test
    fun `every type is writable`() {
        assertTrue(HealthType.entries.all { it.isWritable })
    }

    @Test
    fun `read and write permissions name the same data`() {
        HealthType.entries.forEach {
            assertTrue(it.readPermission.startsWith("android.permission.health.READ_"), it.readPermission)
            assertEquals(it.readPermission.replace(".READ_", ".WRITE_"), it.writePermission, it.name)
        }
    }

    @Test
    fun `only cadence and menstruation types share a permission with another type`() {
        val expected =
            mapOf(
                "android.permission.health.READ_EXERCISE" to
                    listOf(HealthType.CYCLING_PEDALING_CADENCE, HealthType.EXERCISE_SESSION),
                "android.permission.health.READ_MENSTRUATION" to
                    listOf(HealthType.MENSTRUATION_FLOW, HealthType.MENSTRUATION_PERIOD),
                "android.permission.health.READ_STEPS" to listOf(HealthType.STEPS, HealthType.STEPS_CADENCE),
            )

        assertEquals(expected, HealthType.entries.groupBy { it.readPermission }.filterValues { it.size > 1 })
    }

    @Test
    fun `feature gated types name their feature`() {
        val expected =
            mapOf(
                HealthType.MINDFULNESS_SESSION to Feature.MINDFULNESS_SESSION,
                HealthType.PLANNED_EXERCISE_SESSION to Feature.PLANNED_EXERCISE,
                HealthType.SKIN_TEMPERATURE to Feature.SKIN_TEMPERATURE,
            )

        assertEquals(expected, HealthType.entries.filter { it.feature != null }.associateWith { it.feature })
    }

    @Test
    fun `features map to their client constants`() {
        val expected =
            mapOf(
                Feature.READ_HEALTH_DATA_IN_BACKGROUND to HealthConnectFeatures.FEATURE_READ_HEALTH_DATA_IN_BACKGROUND,
                Feature.SKIN_TEMPERATURE to HealthConnectFeatures.FEATURE_SKIN_TEMPERATURE,
                Feature.PLANNED_EXERCISE to HealthConnectFeatures.FEATURE_PLANNED_EXERCISE,
                Feature.READ_HEALTH_DATA_HISTORY to HealthConnectFeatures.FEATURE_READ_HEALTH_DATA_HISTORY,
                Feature.MINDFULNESS_SESSION to HealthConnectFeatures.FEATURE_MINDFULNESS_SESSION,
            )

        assertEquals(expected, Feature.entries.associateWith { it.constant })
    }

    @Test
    fun `aggregations are named after their type and listed by it`() {
        Aggregation.entries.forEach {
            assertTrue(it.name.startsWith(it.healthType.name + "_"), it.name)
            assertTrue(it in it.healthType.aggregations, it.name)
            assertTrue(it.unit.isNotBlank(), it.name)
        }
        assertEquals(Aggregation.entries.size, HealthType.entries.sumOf { it.aggregations.size })
    }
}
