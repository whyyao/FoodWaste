package com.example.foodwaste

import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.foodwaste.databinding.ActivityMainBinding
import com.example.foodwaste.storage.StockFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // force bright mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // setting gesture navigation bar color
        val window: Window = this@MainActivity.window
        window.navigationBarColor =
            ContextCompat.getColor(this@MainActivity, R.color.gesture_navigation_color)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val shoppingFragment = ShoppingFragment()
        val stockFragment = StockFragment()

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAnchorView(R.id.fab)
//                    .setAction("Action", null).show()
//        }


        val sharedPref = getPreferences(
            Context.MODE_PRIVATE
        )
        sharedPref.edit().clear().apply()

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
                else -> false
            }
        }
    }
}