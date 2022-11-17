package com.happyhappyyay.badnutrition.data.filter

data class NutritionFilter(
    val filterName: String = "filter",
    val itemName: String,
    val comparison: String,
    val amount: String,
    val unit: String
) {
    override fun toString(): String {
        return "$itemName $comparison $amount $unit"
    }
}
