package com.kvs.healthconnectreporter.model.type

import kotlinx.serialization.Serializable

/**
 * **MealType** the meal a record belongs to.
 *
 * Mirrors Health Connect's `MealType.MEAL_TYPE_*` constants entry for entry.
 */
@Serializable
public enum class MealType {
    UNKNOWN,
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
}
