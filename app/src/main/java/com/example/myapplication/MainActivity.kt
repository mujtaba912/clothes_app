package com.example.myapplication

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.Api
import com.example.myapplication.data.ClothesData
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    companion object {
        private const val SHAREDPREFERENCESKEY = "SharedPreferencesKey"
        private const val SAVEDTIMEKEY = "SavedTimeKey"
        private const val IMAGEKEY = "ImageKey"
        private const val oneDay = 24 * 60 * 60 * 1000
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHAREDPREFERENCESKEY, MODE_PRIVATE)

        Api().makeRequest { temperatureData, error ->
            if (temperatureData != null) {
                val temperature = temperatureData.data.timelines.intervals[0].values.temperature
                runOnUiThread {
                    binding.textViewTempter.text = temperature.toString()
                    clothesPicker(temperature)
                }
            }
        }
    }

    private fun hasDayPassed(savedTime: Long) = (System.currentTimeMillis() - oneDay > savedTime)

    private fun saveImage(clothes: List<Int>) {

        var image = clothes.random()
        var saveTime = sharedPreferences.getLong(
            SAVEDTIMEKEY,
            0
        )
        var last = sharedPreferences.getInt(
            IMAGEKEY,
            ClothesData().imageNotFound
        )

        if (hasDayPassed(saveTime)) {
            sharedPreferences.edit().putLong(
                SAVEDTIMEKEY,
                System.currentTimeMillis()
            ).apply()
            while (image == last) {
                image = clothes.random()

            }
            binding.imageViewClothes.setImageResource(image)

            sharedPreferences.edit().putInt(
                IMAGEKEY,
                image
            ).apply()

        } else {
            binding.imageViewClothes.setImageResource(last)
        }
    }

    fun clothesPicker(temperature: Double) {
        when {
            temperature > 20 -> {
                saveImage(ClothesData().clothesHot)
            }
            temperature in 0.0..20.0 -> {
                saveImage(ClothesData().clothesRegular)
            }
            temperature < 0 -> {
                saveImage(ClothesData().clothesCold)
            }
        }
    }
}




