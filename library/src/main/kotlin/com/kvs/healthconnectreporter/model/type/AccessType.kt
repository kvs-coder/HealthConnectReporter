package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/** **AccessType** what a data permission allows */
@Serializable
public enum class AccessType {
    /** read, aggregate and read changes */
    READ,

    /** insert, update and delete */
    WRITE,
}
