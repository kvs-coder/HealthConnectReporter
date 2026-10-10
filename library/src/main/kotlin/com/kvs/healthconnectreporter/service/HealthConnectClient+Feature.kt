package com.kvs.healthconnectreporter.service

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectFeatures
import com.kvs.healthconnectreporter.HealthConnectException
import com.kvs.healthconnectreporter.decorator.clientCall
import com.kvs.healthconnectreporter.decorator.constant
import com.kvs.healthconnectreporter.model.type.Feature
import com.kvs.healthconnectreporter.model.type.HealthType

/** **Boolean** the Health Connect on this device supports [feature] */
internal suspend fun HealthConnectClient.isAvailable(feature: Feature): Boolean =
    clientCall { features.getFeatureStatus(feature.constant) } == HealthConnectFeatures.FEATURE_STATUS_AVAILABLE

/** Throws when [type] needs a Health Connect feature this device lacks. */
internal suspend fun HealthConnectClient.checkFeature(type: HealthType) {
    val feature = type.feature ?: return
    if (!isAvailable(feature)) {
        throw HealthConnectException.NotAvailable("${type.identifier} needs Health Connect feature $feature")
    }
}
