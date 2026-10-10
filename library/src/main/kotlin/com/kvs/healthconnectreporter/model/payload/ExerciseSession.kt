package com.kvs.healthconnectreporter.model.payload

import androidx.health.connect.client.records.ExerciseLap
import androidx.health.connect.client.records.ExerciseRouteResult
import androidx.health.connect.client.records.ExerciseSegment
import androidx.health.connect.client.records.ExerciseSessionRecord
import com.kvs.healthconnectreporter.decorator.asExerciseSegmentType
import com.kvs.healthconnectreporter.decorator.asExerciseType
import com.kvs.healthconnectreporter.decorator.asInstant
import com.kvs.healthconnectreporter.decorator.asLength
import com.kvs.healthconnectreporter.decorator.asOriginal
import com.kvs.healthconnectreporter.decorator.asZoneOffset
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.decorator.encoded
import com.kvs.healthconnectreporter.decorator.epochMillis
import com.kvs.healthconnectreporter.decorator.harmonized
import com.kvs.healthconnectreporter.decorator.lastModifiedTimestamp
import com.kvs.healthconnectreporter.decorator.metadata
import com.kvs.healthconnectreporter.decorator.meters
import com.kvs.healthconnectreporter.decorator.sampleClientRecordVersion
import com.kvs.healthconnectreporter.decorator.sampleDataOrigin
import com.kvs.healthconnectreporter.decorator.sampleDevice
import com.kvs.healthconnectreporter.decorator.sampleId
import com.kvs.healthconnectreporter.decorator.sampleRecordingMethod
import com.kvs.healthconnectreporter.decorator.seconds
import com.kvs.healthconnectreporter.decorator.validateInside
import com.kvs.healthconnectreporter.decorator.validateInterval
import com.kvs.healthconnectreporter.model.DataOrigin
import com.kvs.healthconnectreporter.model.Device
import com.kvs.healthconnectreporter.model.Payload
import com.kvs.healthconnectreporter.model.type.ExerciseSegmentType
import com.kvs.healthconnectreporter.model.type.ExerciseType
import com.kvs.healthconnectreporter.model.type.HealthType
import com.kvs.healthconnectreporter.model.type.RecordingMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * **ExerciseSession** an exercise session with its segments, laps and route.
 *
 * @param startTimestamp **Long** epoch milliseconds
 * @param endTimestamp **Long** epoch milliseconds, not before [startTimestamp]
 * @param harmonized **Harmonized** the measured values
 * @param id **String?** Health Connect record id; null until inserted
 * @param clientRecordId **String?** your own id for the record
 * @param clientRecordVersion **Long?** your own version of [clientRecordId]
 * @param startZoneOffsetSeconds **Int?** zone offset at the start in seconds
 * @param endZoneOffsetSeconds **Int?** zone offset at the end in seconds
 * @param recordingMethod **RecordingMethod?** how the data was recorded; manual entry when null
 * @param device **Device?** the recording device
 * @param dataOrigin **DataOrigin?** the writing app; set by Health Connect
 * @param lastModifiedTimestamp **Long?** last modification, epoch milliseconds; set by Health Connect
 * @throws com.kvs.healthconnectreporter.HealthConnectException.InvalidValue when [endTimestamp] is before
 * [startTimestamp] or a segment, lap or route location lies outside the interval
 */
@Serializable
@SerialName("exercise")
public data class ExerciseSession(
    override val startTimestamp: Long,
    override val endTimestamp: Long,
    val harmonized: Harmonized,
    override val id: String? = null,
    override val clientRecordId: String? = null,
    override val clientRecordVersion: Long? = null,
    override val startZoneOffsetSeconds: Int? = null,
    override val endZoneOffsetSeconds: Int? = null,
    override val recordingMethod: RecordingMethod? = null,
    override val device: Device? = null,
    override val dataOrigin: DataOrigin? = null,
    override val lastModifiedTimestamp: Long? = null,
) : WritableSample {
    init {
        validateInterval()
        harmonized.segments.forEach { validateInside(endTimestamp, it.startTimestamp, it.endTimestamp) }
        harmonized.laps.forEach { validateInside(endTimestamp, it.startTimestamp, it.endTimestamp) }
        harmonized.route?.let { route -> route.locations.forEach { validateInside(endTimestamp, it.timestamp) } }
    }

    override val healthType: HealthType get() = HealthType.EXERCISE_SESSION

    override val json: String get() = encoded

    /**
     * **Harmonized** the values of ExerciseSession.
     *
     * @param exerciseType **ExerciseType** the type of exercise
     * @param title **String?** the title of the session
     * @param notes **String?** notes on the session
     * @param segments **List<Segment>** the segments, inside the session and compatible with [exerciseType].
     * Empty by default
     * @param laps **List<Lap>** the laps, inside the session. Empty by default
     * @param route **Route?** the route; null when there is none or reading it needs the user's consent
     * @param isRouteConsentRequired **Boolean** a route exists, but reading it needs the user's consent;
     * set by Health Connect
     * @param plannedExerciseSessionId **String?** id of the **PlannedExerciseSession** this session completes
     */
    @Serializable
    public data class Harmonized(
        val exerciseType: ExerciseType,
        val title: String? = null,
        val notes: String? = null,
        val segments: List<Segment> = emptyList(),
        val laps: List<Lap> = emptyList(),
        val route: Route? = null,
        val isRouteConsentRequired: Boolean = false,
        val plannedExerciseSessionId: String? = null,
    )

    /**
     * **Segment** one segment of the session, e.g. a set of an exercise.
     *
     * @param startTimestamp **Long** epoch milliseconds
     * @param endTimestamp **Long** epoch milliseconds, after [startTimestamp]
     * @param segmentType **ExerciseSegmentType** the type of segment
     * @param repetitions **Int** number of repetitions, not negative. 0 by default
     * @throws com.kvs.healthconnectreporter.HealthConnectException.InvalidValue when [endTimestamp] is before
     * [startTimestamp]
     */
    @Serializable
    public data class Segment(
        val startTimestamp: Long,
        val endTimestamp: Long,
        val segmentType: ExerciseSegmentType,
        val repetitions: Int = 0,
    ) {
        init {
            startTimestamp.validateInterval(endTimestamp)
        }
    }

    /**
     * **Lap** one lap of the session.
     *
     * @param startTimestamp **Long** epoch milliseconds
     * @param endTimestamp **Long** epoch milliseconds, after [startTimestamp]
     * @param length **Double?** length of the lap in m (0–1 000 000)
     * @throws com.kvs.healthconnectreporter.HealthConnectException.InvalidValue when [endTimestamp] is before
     * [startTimestamp]
     */
    @Serializable
    public data class Lap(
        val startTimestamp: Long,
        val endTimestamp: Long,
        val length: Double? = null,
    ) {
        init {
            startTimestamp.validateInterval(endTimestamp)
        }
    }

    /**
     * **Route** the route of the session.
     *
     * @param locations **List<Location>** the locations, inside the session
     */
    @Serializable
    public data class Route(
        val locations: List<Location>,
    )

    /**
     * **Location** one point of a route.
     *
     * @param timestamp **Long** epoch milliseconds
     * @param latitude **Double** latitude in degrees (-90–90)
     * @param longitude **Double** longitude in degrees (-180–180)
     * @param horizontalAccuracy **Double?** horizontal accuracy in m
     * @param verticalAccuracy **Double?** vertical accuracy in m
     * @param altitude **Double?** altitude in m
     */
    @Serializable
    public data class Location(
        val timestamp: Long,
        val latitude: Double,
        val longitude: Double,
        val horizontalAccuracy: Double? = null,
        val verticalAccuracy: Double? = null,
        val altitude: Double? = null,
    )

    // region Original
    internal fun asOriginal(): ExerciseSessionRecord =
        ExerciseSessionRecord(
            startTime = startTimestamp.asInstant,
            startZoneOffset = startZoneOffsetSeconds.asZoneOffset,
            endTime = endTimestamp.asInstant,
            endZoneOffset = endZoneOffsetSeconds.asZoneOffset,
            metadata = metadata(),
            exerciseType = harmonized.exerciseType.constant,
            title = harmonized.title,
            notes = harmonized.notes,
            segments =
                harmonized.segments.map {
                    ExerciseSegment(
                        startTime = it.startTimestamp.asInstant,
                        endTime = it.endTimestamp.asInstant,
                        segmentType = it.segmentType.constant,
                        repetitions = it.repetitions,
                    )
                },
            laps =
                harmonized.laps.map {
                    ExerciseLap(
                        startTime = it.startTimestamp.asInstant,
                        endTime = it.endTimestamp.asInstant,
                        length = it.length?.asLength,
                    )
                },
            exerciseRoute = harmonized.route?.asOriginal,
            plannedExerciseSessionId = harmonized.plannedExerciseSessionId,
        )
    // endregion

    /** Factory of **ExerciseSession** */
    public companion object : Payload.Factory<ExerciseSession>({ ExerciseSession.serializer() }) {
        internal fun from(record: ExerciseSessionRecord): ExerciseSession =
            ExerciseSession(
                startTimestamp = record.startTime.epochMillis,
                endTimestamp = record.endTime.epochMillis,
                harmonized =
                    Harmonized(
                        exerciseType = record.exerciseType.asExerciseType,
                        title = record.title,
                        notes = record.notes,
                        segments =
                            record.segments.map {
                                Segment(
                                    startTimestamp = it.startTime.epochMillis,
                                    endTimestamp = it.endTime.epochMillis,
                                    segmentType = it.segmentType.asExerciseSegmentType,
                                    repetitions = it.repetitions,
                                )
                            },
                        laps =
                            record.laps.map {
                                Lap(
                                    startTimestamp = it.startTime.epochMillis,
                                    endTimestamp = it.endTime.epochMillis,
                                    length = it.length?.meters,
                                )
                            },
                        route =
                            (record.exerciseRouteResult as? ExerciseRouteResult.Data)?.let {
                                it.exerciseRoute.harmonized
                            },
                        isRouteConsentRequired = record.exerciseRouteResult is ExerciseRouteResult.ConsentRequired,
                        plannedExerciseSessionId = record.plannedExerciseSessionId,
                    ),
                id = record.metadata.sampleId,
                clientRecordId = record.metadata.clientRecordId,
                clientRecordVersion = record.metadata.sampleClientRecordVersion,
                startZoneOffsetSeconds = record.startZoneOffset.seconds,
                endZoneOffsetSeconds = record.endZoneOffset.seconds,
                recordingMethod = record.metadata.sampleRecordingMethod,
                device = record.metadata.sampleDevice,
                dataOrigin = record.metadata.sampleDataOrigin,
                lastModifiedTimestamp = record.metadata.lastModifiedTimestamp,
            )
    }
}
