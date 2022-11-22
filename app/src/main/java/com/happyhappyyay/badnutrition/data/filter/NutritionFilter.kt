package com.happyhappyyay.badnutrition.data.filter

data class NutritionFilter(
    val filterName: String = "filter",
    val itemName: String,
    val comparison: String,
) {
    constructor(
        filterName: String = "filter",
        itemName: String,
        comparison: String,
        amount: String,
    ) : this(filterName, itemName, comparison)

    constructor(
        filterName: String = "filter",
        itemName: String,
        comparison: String,
        amount: String,
        unit: String
    ) : this(filterName, itemName, comparison, amount)

    override fun toString(): String {
        return "$itemName $comparison"
    }
}