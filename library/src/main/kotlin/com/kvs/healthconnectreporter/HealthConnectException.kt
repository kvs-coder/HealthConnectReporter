package com.kvs.healthconnectreporter

/**
 * The single error type thrown by **HealthConnectReporter**.
 *
 * Health Connect client exceptions are wrapped and kept as [cause].
 */
public sealed class HealthConnectException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {
    /** The user has not granted the permission the call needs. */
    public class NotAuthorized(
        message: String,
        cause: Throwable? = null,
    ) : HealthConnectException(message, cause)

    /** A type does not support the requested operation, or can't be mapped. */
    public class InvalidType(
        message: String,
        cause: Throwable? = null,
    ) : HealthConnectException(message, cause)

    /** An input value is invalid, or a stored value can't be converted. */
    public class InvalidValue(
        message: String,
        cause: Throwable? = null,
    ) : HealthConnectException(message, cause)

    /**
     * Health Connect, or a feature the call needs, is not available on this device.
     * [HealthConnectReporter.availability] tells whether installing or updating Health Connect helps.
     */
    public class NotAvailable(
        message: String,
        cause: Throwable? = null,
    ) : HealthConnectException(message, cause)

    /** The changes anchor expired (Health Connect keeps changes for 30 days); read the data again in full. */
    public class AnchorExpired(
        message: String,
        cause: Throwable? = null,
    ) : HealthConnectException(message, cause)

    /** Any other Health Connect error (remote, I/O, rate limit). */
    public class Platform(
        message: String,
        cause: Throwable? = null,
    ) : HealthConnectException(message, cause)
}
