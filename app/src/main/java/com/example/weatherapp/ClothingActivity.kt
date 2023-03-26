package com.example.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_clothing.*

class ClothingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothing)

        temp.text = intent.getStringExtra("degree")
        status.text = intent.getStringExtra("status")
        humidityContent.text = intent.getStringExtra("humidity")
        windContent.text = intent.getStringExtra("wind")
        rainContent.text = intent.getStringExtra("rain")

        message1.text = "Today is ${status.text}, clothes options for you:"

        if (windContent.text.toString() > 10.0.toString()) {
            headText.text = "Today is windy so you should wear a beanie and a scarf"
        } else {
            headText.text = "Today is not windy so you don't need to wear a beanie and a scarf"
        }

        if (status.text == "Rainy") {
            message1.text = "Today is rainy so you should wear a raincoat and an umbrella"
            shoeText.text = "Today is rainy so you should wear boots"
        } else {
            shoeText.text = "Today you can wear a sneaker"
        }

        if (status.text.toString() == "Clear") {
            message1.text = "Today is cloudy so you should wear sunglasses"
        }

        if (temp.text.toString() > "25°C") {
            bottomText.text = "Today is hot so you should wear a short"
        } else {
            bottomText.text = "Today is not hot so you should wear a pants, jeans or leggings"
        }

        if (temp.text.toString() <= "10°C") {
            topText.text = "Today is very cold so you should wear a sweater and a coat"
        }
        if (temp.text.toString() > "10°C" && temp.text.toString() < "20°C") {
            topText.text = "Today is cold so you should wear a sweater and jacket or shirt"

        }
        if (temp.text.toString() > "20°C" && temp.text.toString() < "25°C") {
            topText.text = "Today is warm so you should wear a t-shirt and jacket or shirt"
        }
        if (temp.text.toString() > "25°C") {
            topText.text = "Today is hot so you should wear a t-shirt"
        }
    }
}