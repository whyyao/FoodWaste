package com.example.foodwaste

import java.text.SimpleDateFormat
import java.util.*

object StringUtils {

    fun getPrettyDate(apiDate: String): String {
        val apiDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val apiDate: Date = apiDateFormat.parse(apiDate)
        val displayDateFormat = SimpleDateFormat("MMM dd")
        return displayDateFormat.format(apiDate)
    }
}