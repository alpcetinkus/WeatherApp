package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.service.WeatherService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchWeatherMap()
    }

    fun fetchWeatherMap() {
        WeatherService.getApiImplementation().getData().enqueue(object : Callback<WeatherModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                val resBody = response.body()

                if (resBody != null) {
                    val dateSunrise = Date(resBody.sys.sunrise * 1000L)
                    val dateSunset = Date(resBody.sys.sunset * 1000L)
                    val format = SimpleDateFormat("hh:mm aa", Locale.getDefault())
                    addressText.text = resBody.name
                    country.text = resBody.sys.country
                    temp.text = "${(resBody.main.temp/10).toInt()}°C"
                    status.text = resBody.weather[0].main
                    wind.text = resBody.wind.speed.toString()
                    pressure.text = resBody.main.pressure.toString()
                    humidity.text = resBody.main.humidity.toString()
                    sunset.text = format.format(dateSunset)
                    sunrise.text = format.format(dateSunrise)
                    rain.text = resBody.rain?.`1h`?.toString() ?: "0"

                }

            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Log.e("alppppp", "Failed::" + (t))
            }

        })
    }
}