package com.kvs.healthconnectreporter.decorator

import android.os.RemoteException
import com.kvs.healthconnectreporter.HealthConnectException
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Runs a Health Connect client call and the request building before it, rethrowing client exceptions as
 * **HealthConnectException** with the original as cause.
 */
internal suspend fun <T> clientCall(block: suspend () -> T): T =
    try {
        block()
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: SecurityException) {
        throw HealthConnectException.NotAuthorized("Health Connect permission missing: ${exception.message}", exception)
    } catch (exception: IllegalArgumentException) {
        throw HealthConnectException.InvalidValue("Health Connect rejected the input: ${exception.message}", exception)
    } catch (exception: UnsupportedOperationException) {
        throw HealthConnectException.NotAvailable("Health Connect does not support: ${exception.message}", exception)
    } catch (exception: RemoteException) {
        throw HealthConnectException.Platform("Health Connect failed: ${exception.message}", exception)
    } catch (exception: IOException) {
        throw HealthConnectException.Platform("Health Connect failed: ${exception.message}", exception)
    } catch (exception: IllegalStateException) {
        throw HealthConnectException.Platform("Health Connect failed: ${exception.message}", exception)
    }
