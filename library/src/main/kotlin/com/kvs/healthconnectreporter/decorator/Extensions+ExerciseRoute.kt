package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.ExerciseRoute
import com.kvs.healthconnectreporter.model.payload.ExerciseSession

internal val ExerciseSession.Route.asOriginal: ExerciseRoute
    get() =
        ExerciseRoute(
            locations.map {
                ExerciseRoute.Location(
                    time = it.timestamp.asInstant,
                    latitude = it.latitude,
                    longitude = it.longitude,
                    horizontalAccuracy = it.horizontalAccuracy?.asLength,
                    verticalAccuracy = it.verticalAccuracy?.asLength,
                    altitude = it.altitude?.asLength,
                )
            },
        )

internal val ExerciseRoute.harmonized: ExerciseSession.Route
    get() =
        ExerciseSession.Route(
            locations =
                route.map {
                    ExerciseSession.Location(
                        timestamp = it.time.epochMillis,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        horizontalAccuracy = it.horizontalAccuracy?.meters,
                        verticalAccuracy = it.verticalAccuracy?.meters,
                        altitude = it.altitude?.meters,
                    )
                },
        )
