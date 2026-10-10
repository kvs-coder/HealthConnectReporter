package com.kvs.healthconnectreporter.service

import android.os.RemoteException
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.clientCall
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class ClientCallTest {
    @Test
    fun `client exceptions are wrapped with their cause`() =
        runTest {
            val expected =
                mapOf<Exception, (HealthConnectException) -> Unit>(
                    SecurityException("a") to { assertIs<HealthConnectException.NotAuthorized>(it) },
                    IllegalArgumentException("b") to { assertIs<HealthConnectException.InvalidValue>(it) },
                    UnsupportedOperationException("c") to { assertIs<HealthConnectException.NotAvailable>(it) },
                    RemoteException("d") to { assertIs<HealthConnectException.Platform>(it) },
                    IOException("e") to { assertIs<HealthConnectException.Platform>(it) },
                    IllegalStateException("f") to { assertIs<HealthConnectException.Platform>(it) },
                )

            expected.forEach { (cause, check) ->
                val error = assertFailsWith<HealthConnectException> { clientCall { throw cause } }
                check(error)
                assertSame(cause, error.cause)
            }
        }

    @Test
    fun `cancellation is not wrapped`() =
        runTest {
            val cancellation = CancellationException("cancelled")

            val error = assertFailsWith<CancellationException> { clientCall { throw cancellation } }

            assertSame(cancellation, error)
        }

    @Test
    fun `library exceptions pass through`() =
        runTest {
            val invalid = HealthConnectException.InvalidValue("invalid")

            assertSame(invalid, assertFailsWith<HealthConnectException> { clientCall { throw invalid } })
        }

    @Test
    fun `results pass through`() =
        runTest {
            assertEquals(1, clientCall { 1 })
        }
}
