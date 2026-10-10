package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **RelationToMeal** when a blood glucose level was measured relative to a meal.
 *
 * Mirrors Health Connect's `BloodGlucoseRecord.RELATION_TO_MEAL_*` constants entry for entry.
 */
@Serializable
public enum class RelationToMeal {
    UNKNOWN,
    GENERAL,
    FASTING,
    BEFORE_MEAL,
    AFTER_MEAL,
}
