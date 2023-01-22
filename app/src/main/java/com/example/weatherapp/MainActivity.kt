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
                    addressText.text = resBody.name
                    country.text = resBody.sys.country
                    temp.text = "${resBody.main.temp}°C "
                    status.text = resBody.weather.main
                    wind.text = resBody.wind.speed.toString()
                    pressure.text = resBody.main.pressure.toString()
                    humidity.text = resBody.main.humidity.toString()
                    sunset.text = resBody.sys.sunset.toString()
                    sunrise.text = resBody.sys.sunrise.toString()

                }

            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Log.e("alppppp", "Failed::" + (t))
            }

        })
    }
}