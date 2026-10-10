package com.kvs.healthconnectreporter.type

import com.kvs.healthconnectreporter.END
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.START
import com.kvs.healthconnectreporter.device
import com.kvs.healthconnectreporter.heartRate
import com.kvs.healthconnectreporter.model.Aggregate
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Page
import com.kvs.healthconnectreporter.model.TimeGroup
import com.kvs.healthconnectreporter.model.TimeRange
import com.kvs.healthconnectreporter.model.type.Aggregation
import com.kvs.healthconnectreporter.model.type.TimeGroupUnit
import com.kvs.healthconnectreporter.steps
import kotlinx.serialization.json.Json
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValueTypeTest {
    private val zone = ZoneId.of("Europe/Berlin")

    @Test
    fun `value payloads round trip through json and maps`() {
        val range = TimeRange(START, END)
        val origin = DataOrigin("com.example")

        assertEquals(device, Device.decode(device.json))
        assertEquals(range, TimeRange.decode(range.json))
        assertEquals(origin, DataOrigin.decode(origin.json))
        assertEquals(range, TimeRange.make(mapOf("startTimestamp" to START, "endTimestamp" to END)))
        assertEquals(Device(), Device.make(emptyMap()))
    }

    @Test
    fun `time range must end after its start`() {
        listOf(START, START - 1).forEach { end ->
            val error = assertFailsWith<HealthConnectException.InvalidValue> { TimeRange(START, end) }
            assertEquals("Invalid TimeRange: endTimestamp $end is not after startTimestamp $START", error.message)
            assertFailsWith<HealthConnectException.InvalidValue> {
                TimeRange.make(mapOf("startTimestamp" to START, "endTimestamp" to end))
            }
        }
    }

    @Test
    fun `day spans the calendar day in the zone`() {
        val noon = Instant.parse("2026-10-10T10:00:00Z").toEpochMilli()

        val sut = TimeRange.day(noon, zone)

        assertEquals(Instant.parse("2026-10-09T22:00:00Z").toEpochMilli(), sut.startTimestamp)
        assertEquals(Instant.parse("2026-10-10T22:00:00Z").toEpochMilli(), sut.endTimestamp)
    }

    @Test
    fun `last days include today`() {
        val noon = Instant.parse("2026-10-10T12:00:00Z").toEpochMilli()

        val sut = TimeRange.lastDays(7, noon, ZoneOffset.UTC)

        assertEquals(Instant.parse("2026-10-04T00:00:00Z").toEpochMilli(), sut.startTimestamp)
        assertEquals(Instant.parse("2026-10-11T00:00:00Z").toEpochMilli(), sut.endTimestamp)
    }

    @Test
    fun `last days defaults to now in the device zone`() {
        val sut = TimeRange.day()

        assertEquals(true, System.currentTimeMillis() in sut.startTimestamp until sut.endTimestamp)
        assertEquals(TimeRange.lastDays(1), sut)
    }

    @Test
    fun `last days needs at least one day`() {
        val error = assertFailsWith<HealthConnectException.InvalidValue> { TimeRange.lastDays(0) }

        assertEquals("Invalid days: 0", error.message)
    }

    @Test
    fun `time groups validate their values`() {
        listOf(
            { TimeGroup.Duration(0) },
            { TimeGroup.Period(TimeGroupUnit.DAILY, 0) },
            { TimeGroup.Period(TimeGroupUnit.DAILY, 1, "Mars/Olympus") },
        ).forEach { assertFailsWith<HealthConnectException.InvalidValue> { it() } }
        listOf(
            """{"type":"duration","durationMillis":0}""",
            """{"type":"period","unit":"DAILY","count":0}""",
        ).forEach {
            assertFailsWith<HealthConnectException.InvalidValue> { Json.decodeFromString(TimeGroup.serializer(), it) }
        }
    }

    @Test
    fun `time groups round trip through json`() {
        val json = Json
        listOf(TimeGroup.Duration(60_000), TimeGroup.Period(TimeGroupUnit.MONTHLY)).forEach {
            assertEquals(
                it,
                json.decodeFromString(TimeGroup.serializer(), json.encodeToString(TimeGroup.serializer(), it)),
            )
        }
    }

    @Test
    fun `page and aggregate round trip through json`() {
        val page = Page(listOf(steps, heartRate), "next")
        val aggregate = Aggregate(Aggregation.STEPS_COUNT_TOTAL, START, END, 1200.0)

        assertEquals(page, Page.decode(page.json))
        assertEquals(aggregate, Aggregate.decode(aggregate.json))
        assertEquals("count", aggregate.unit)
    }
}
