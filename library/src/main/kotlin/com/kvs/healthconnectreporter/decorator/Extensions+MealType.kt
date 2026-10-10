package com.kvs.healthconnectreporter.decorator

import com.kvs.healthconnectreporter.model.type.MealType
import androidx.health.connect.client.records.MealType as OriginalMealType

private val mealTypeConstants =
    ConstantFamily(
        MealType.UNKNOWN,
        OriginalMealType.MEAL_TYPE_UNKNOWN to MealType.UNKNOWN,
        OriginalMealType.MEAL_TYPE_BREAKFAST to MealType.BREAKFAST,
        OriginalMealType.MEAL_TYPE_LUNCH to MealType.LUNCH,
        OriginalMealType.MEAL_TYPE_DINNER to MealType.DINNER,
        OriginalMealType.MEAL_TYPE_SNACK to MealType.SNACK,
    )

internal val MealType.constant: Int
    get() = mealTypeConstants.constant(this)

internal val Int.asMealType: MealType
    get() = mealTypeConstants.entry(this)
