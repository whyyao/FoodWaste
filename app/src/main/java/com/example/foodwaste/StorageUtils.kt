package com.example.foodwaste

import android.app.Activity
import android.content.Context
import com.example.foodwaste.model.FoodItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object StorageUtils {

    fun getExpiringFoodList(activity: Activity): List<FoodItem> {
        val sharedPref = activity.getPreferences(
            Context.MODE_PRIVATE
        )
        val listType: Type = object : TypeToken<List<FoodItem?>?>() {}.type
        val gson = Gson()
        return gson.fromJson<List<FoodItem>>(sharedPref.getString("expiring", ""), listType)
            .orEmpty()
    }

    fun getStockFoodList(activity: Activity): List<FoodItem> {
        val sharedPref = activity.getPreferences(
            Context.MODE_PRIVATE
        )
        val listType: Type = object : TypeToken<List<FoodItem?>?>() {}.type
        val gson = Gson()
        return gson.fromJson<List<FoodItem>>(sharedPref.getString("stock", ""), listType)
            .orEmpty()
    }

    fun saveToExpiringFoodList(activity: Activity, list: List<FoodItem>) {
        val sharedPref = activity.getPreferences(
            Context.MODE_PRIVATE
        )
        sharedPref?.edit()?.putString("expiring", Gson().toJson(list))?.apply()
    }

    fun saveToStockFoodList(activity: Activity, list: List<FoodItem>) {
        val sharedPref = activity.getPreferences(
            Context.MODE_PRIVATE
        )
        sharedPref?.edit()?.putString("stock", Gson().toJson(list))?.apply()
    }
}