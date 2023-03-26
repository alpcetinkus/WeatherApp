package com.example.weatherapp.service

import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    // https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}
    @GET("data/2.5/weather")
    fun getData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Call<WeatherModel>

    // https://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
    @GET("data/2.5/forecast")
    fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Call<Forecast>
}