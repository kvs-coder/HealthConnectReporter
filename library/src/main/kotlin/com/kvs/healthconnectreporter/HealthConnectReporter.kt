package com.kvs.healthconnectreporter

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.kvs.healthconnectreporter.model.Availability
import com.kvs.healthconnectreporter.service.HealthConnectReader

/**
 * **HealthConnectReporter** entry point to Health Connect.
 *
 * Create one per app (or per screen) and keep it; every service shares one Health Connect client.
 * All calls are `suspend` and run on the caller's coroutine context.
 */
public class HealthConnectReporter internal constructor(
    internal val client: HealthConnectClient,
) {
    /** **HealthConnectReader** reads and aggregates data */
    public val reader: HealthConnectReader = HealthConnectReader(client)

    /**
     * Connects to Health Connect.
     *
     * @param context **Context** any context; the application context is used
     * @throws HealthConnectException.NotAvailable when [availability] is not [Availability.AVAILABLE]
     */
    public constructor(context: Context) : this(connect(context.applicationContext))

    /** Availability checks that need no connection */
    public companion object {
        /**
         * Checks whether Health Connect can be used on this device.
         *
         * @param context **Context** any context
         * @return **Availability** [Availability.AVAILABLE] when Health Connect is ready to use
         */
        public fun availability(context: Context): Availability =
            when (HealthConnectClient.getSdkStatus(context)) {
                HealthConnectClient.SDK_AVAILABLE -> Availability.AVAILABLE
                HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> Availability.UPDATE_REQUIRED
                else -> Availability.UNAVAILABLE
            }

        private fun connect(context: Context): HealthConnectClient {
            val availability = availability(context)
            if (availability != Availability.AVAILABLE) {
                throw HealthConnectException.NotAvailable("Health Connect is not available: $availability")
            }
            return HealthConnectClient.getOrCreate(context)
        }
    }
}
