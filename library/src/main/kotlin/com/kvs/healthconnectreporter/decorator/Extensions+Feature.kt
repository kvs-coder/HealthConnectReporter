@file:OptIn(ExperimentalMindfulnessSessionApi::class)

package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.HealthConnectFeatures
import androidx.health.connect.client.feature.ExperimentalMindfulnessSessionApi
import com.kvs.healthconnectreporter.model.type.Feature

internal val Feature.constant: Int
    get() =
        when (this) {
            Feature.READ_HEALTH_DATA_IN_BACKGROUND -> HealthConnectFeatures.FEATURE_READ_HEALTH_DATA_IN_BACKGROUND
            Feature.SKIN_TEMPERATURE -> HealthConnectFeatures.FEATURE_SKIN_TEMPERATURE
            Feature.PLANNED_EXERCISE -> HealthConnectFeatures.FEATURE_PLANNED_EXERCISE
            Feature.READ_HEALTH_DATA_HISTORY -> HealthConnectFeatures.FEATURE_READ_HEALTH_DATA_HISTORY
            Feature.MINDFULNESS_SESSION -> HealthConnectFeatures.FEATURE_MINDFULNESS_SESSION
        }
