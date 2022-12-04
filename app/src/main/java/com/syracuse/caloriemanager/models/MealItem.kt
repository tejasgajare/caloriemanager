package com.syracuse.caloriemanager.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties("selection")
class MealItem: Serializable {
    val name: String = ""
    val unit: String = ""
    val count: Int = 0
    val calories: Int = 0
    val proteins: Int = 0
    val fats: Int = 0
    val carbohydrates: Int = 0
}