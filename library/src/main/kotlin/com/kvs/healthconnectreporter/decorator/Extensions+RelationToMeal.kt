package com.kvs.healthconnectreporter.decorator

import androidx.health.connect.client.records.BloodGlucoseRecord
import com.kvs.healthconnectreporter.model.type.RelationToMeal

private val relationToMealConstants =
    ConstantFamily(
        RelationToMeal.UNKNOWN,
        BloodGlucoseRecord.RELATION_TO_MEAL_UNKNOWN to RelationToMeal.UNKNOWN,
        BloodGlucoseRecord.RELATION_TO_MEAL_GENERAL to RelationToMeal.GENERAL,
        BloodGlucoseRecord.RELATION_TO_MEAL_FASTING to RelationToMeal.FASTING,
        BloodGlucoseRecord.RELATION_TO_MEAL_BEFORE_MEAL to RelationToMeal.BEFORE_MEAL,
        BloodGlucoseRecord.RELATION_TO_MEAL_AFTER_MEAL to RelationToMeal.AFTER_MEAL,
    )

internal val RelationToMeal.constant: Int
    get() = relationToMealConstants.constant(this)

internal val Int.asRelationToMeal: RelationToMeal
    get() = relationToMealConstants.entry(this)
