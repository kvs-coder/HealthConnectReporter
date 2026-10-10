package com.kvs.healthconnectreporter

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class HealthConnectExceptionTest {
    @Test
    fun `every case keeps message and cause`() {
        val cause = IllegalStateException("client")
        val sut =
            listOf(
                HealthConnectException.NotAuthorized("message", cause),
                HealthConnectException.InvalidType("message", cause),
                HealthConnectException.InvalidValue("message", cause),
                HealthConnectException.NotAvailable("message", cause),
                HealthConnectException.AnchorExpired("message", cause),
                HealthConnectException.Platform("message", cause),
            )

        sut.forEach {
            assertEquals("message", it.message)
            assertSame(cause, it.cause)
        }
    }

    @Test
    fun `cause is optional`() {
        val sut =
            listOf(
                HealthConnectException.NotAuthorized("message"),
                HealthConnectException.InvalidType("message"),
                HealthConnectException.InvalidValue("message"),
                HealthConnectException.NotAvailable("message"),
                HealthConnectException.AnchorExpired("message"),
                HealthConnectException.Platform("message"),
            )

        sut.forEach { assertNull(it.cause) }
    }
}
