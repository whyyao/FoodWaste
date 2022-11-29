package com.example.foodwaste.model

data class FoodItem(
    val name: String = "",
    val expirationDate: String = "",
    val co2: Int = 0,
    var isChecked: Boolean = false
)