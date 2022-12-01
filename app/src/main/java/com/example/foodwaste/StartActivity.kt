package com.example.foodwaste

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.foodwaste.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // force bright mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, MainActivity::class.java)
        binding.activityStartButton1.setOnClickListener {
            intent.putExtra("mode", 1)
            startActivity(intent)
        }

        binding.activityStartButton2.setOnClickListener {
            intent.putExtra("mode", 2)
            startActivity(intent)
        }

        binding.activityStartButton3.setOnClickListener {
            intent.putExtra("mode", 3)
            startActivity(intent)
        }
    }

}