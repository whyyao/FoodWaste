package com.example.foodwaste

import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.foodwaste.databinding.ActivityMainBinding
import com.example.foodwaste.model.FoodItem
import com.example.foodwaste.shopping.ShoppingFragment
import com.example.foodwaste.storage.StockFragment
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // force bright mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shoppingFragment = ShoppingFragment()
        val stockFragment = StockFragment()
        val achievementFragment = AchievementFragment()

        val sharedPref = getPreferences(
            Context.MODE_PRIVATE
        )

        // Clean shared prefs
        sharedPref.edit().clear().apply()
        val gson = Gson()
        val mode: Int = intent.extras?.getInt("mode") ?: 1
        val testExpiringList = if (mode == 1) testExpiringList1 else testExpiringList2
        val testStockList = if (mode == 1) testStockList1 else testStockList2
        with(sharedPref?.edit() ?: return) {
            putString("expiring", gson.toJson(testExpiringList))
            putString("stock", gson.toJson(testStockList))
            apply()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, stockFragment).commit()


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.pg_shopping -> {
                    // Respond to navigation item 1 click
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, shoppingFragment).commit()
                    true
                }
                R.id.pg_storage -> {
                    // Respond to navigation item 2 click
                    supportFragmentManager.beginTransaction().replace(R.id.container, stockFragment)
                        .commit()
                    true
                }
                R.id.pg_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, achievementFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}

val testExpiringList1 = emptyList<FoodItem>()

val testExpiringList2 = listOf(
    FoodItem(
        name = "Cauliflowers",
        expirationDate = "Dec 3",
        co2 = 2
    ),
    FoodItem(
        name = "Onions",
        expirationDate = "Dec 4",
        co2 = 3
    )
)

val testStockList1 = listOf(
    FoodItem(
        name = "Tomatoes",
        expirationDate = "Dec 3",
        co2 = 2
    ),
    FoodItem(
        name = "Corns",
        expirationDate = "Dec 20",
        co2 = 1
    ),
    FoodItem(
        name = "Green Onions",
        expirationDate = "Dec 20",
        co2 = 1
    )
)

val testStockList2 = listOf(
    FoodItem(
        name = "Onions",
        expirationDate = "Dec 3",
        co2 = 2
    ),
    FoodItem(
        name = "Corns",
        expirationDate = "Dec 20",
        co2 = 1
    ),
    FoodItem(
        name = "Green Onions",
        expirationDate = "Dec 20",
        co2 = 1
    )
)