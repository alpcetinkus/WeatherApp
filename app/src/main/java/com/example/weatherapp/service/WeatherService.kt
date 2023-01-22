package com.example.weatherapp.service


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService {
    companion object{
        val BASE_URL = "https://api.openweathermap.org/"
        fun getApiImplementation(): WeatherApi {
            return getClient(BASE_URL).create(WeatherApi::class.java)
        }

        fun getClient(baseUrl: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}