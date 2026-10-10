package com.kvs.healthconnectreporter

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.kvs.healthconnectreporter.model.Availability
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class HealthConnectReporterTest {
    private val context = mockk<Context>()
    private val client = mockk<HealthConnectClient>()

    @Before
    fun setUp() {
        every { context.applicationContext } returns context
        mockkObject(HealthConnectClient)
        every { HealthConnectClient.getOrCreate(context) } returns client
    }

    @After
    fun tearDown() {
        unmockkObject(HealthConnectClient)
    }

    @Test
    fun `availability maps every sdk status`() {
        val expected =
            mapOf(
                HealthConnectClient.SDK_AVAILABLE to Availability.AVAILABLE,
                HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED to Availability.UPDATE_REQUIRED,
                HealthConnectClient.SDK_UNAVAILABLE to Availability.UNAVAILABLE,
                Int.MAX_VALUE to Availability.UNAVAILABLE,
            )
        expected.forEach { (status, availability) ->
            every { HealthConnectClient.getSdkStatus(context) } returns status

            assertEquals(availability, HealthConnectReporter.availability(context), "status $status")
        }
    }

    @Test
    fun `create connects when available`() {
        every { HealthConnectClient.getSdkStatus(context) } returns HealthConnectClient.SDK_AVAILABLE

        val sut = HealthConnectReporter(context)

        assertSame(client, sut.client)
    }

    @Test
    fun `create throws when update is required`() {
        every {
            HealthConnectClient.getSdkStatus(context)
        } returns HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED

        val error = assertFailsWith<HealthConnectException.NotAvailable> { HealthConnectReporter(context) }

        assertEquals("Health Connect is not available: UPDATE_REQUIRED", error.message)
    }
}
