package com.example.foodwaste.model

data class FoodItem(
    val name: String = "",
    val expirationDate: String = "",
    val co2: Double = 0.0,
    var isChecked: Boolean = false,
    var isShared: Boolean = false
)