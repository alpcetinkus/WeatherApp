package com.example.weatherapp.service

import com.example.weatherapp.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET

interface WeatherApi {
//https://api.openweathermap.org/data/2.5/weather?q=marmaris&APPID=75faa8083541a6689f4998dab66234a4
    @GET("data/2.5/weather?q=maltepe&APPID=75faa8083541a6689f4998dab66234a4")
    fun getData(): Call<WeatherModel>
}