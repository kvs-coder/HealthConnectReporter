package com.kvs.healthconnectreporter.model

import com.kvs.healthconnectreporter.model.type.AccessType
import com.kvs.healthconnectreporter.model.type.HealthType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **Permission** one Health Connect permission; JSON carries a `type` discriminator.
 *
 * The consuming app declares each permission it requests in its manifest, e.g.
 * `android.permission.health.READ_STEPS`.
 */
@Serializable
public sealed interface Permission {
    /**
     * **Data** access to the records of one type.
     *
     * Some types share their Health Connect permission: [HealthType.STEPS] and [HealthType.STEPS_CADENCE],
     * [HealthType.EXERCISE_SESSION] and [HealthType.CYCLING_PEDALING_CADENCE], [HealthType.MENSTRUATION_FLOW] and
     * [HealthType.MENSTRUATION_PERIOD].
     *
     * @param healthType **HealthType** the data type
     * @param access **AccessType** read or write
     */
    @Serializable
    @SerialName("data")
    public data class Data(
        val healthType: HealthType,
        val access: AccessType,
    ) : Permission

    /** **ReadInBackground** read data while the app is in the background; needs its feature */
    @Serializable
    @SerialName("read_in_background")
    public data object ReadInBackground : Permission

    /** **ReadHistory** read data older than 30 days before the first grant; needs its feature */
    @Serializable
    @SerialName("read_history")
    public data object ReadHistory : Permission

    /** **WriteExerciseRoute** write exercise routes */
    @Serializable
    @SerialName("write_exercise_route")
    public data object WriteExerciseRoute : Permission

    /** Factory of **Permission** */
    public companion object {
        /**
         * The read and write permissions of types.
         *
         * @param types **Collection<HealthType>** the types
         * @param access **Set<AccessType>** the access to each type. Read and write by default
         * @return **Set<Permission>** one data permission per type and access
         */
        public fun of(
            types: Collection<HealthType>,
            access: Set<AccessType> = AccessType.entries.toSet(),
        ): Set<Permission> = types.flatMapTo(mutableSetOf()) { type -> access.map { Data(type, it) } }
    }
}
