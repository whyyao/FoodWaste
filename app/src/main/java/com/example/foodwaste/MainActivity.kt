package com.example.foodwaste

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.foodwaste.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

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
            .replace(R.id.container, shoppingFragment).commit()


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    // Respond to navigation item 1 click
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, shoppingFragment).commit()
                    true
                }
                R.id.page_2 -> {
                    // Respond to navigation item 2 click
                    supportFragmentManager.beginTransaction().replace(R.id.container, stockFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}