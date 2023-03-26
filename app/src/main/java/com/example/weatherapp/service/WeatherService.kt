package com.example.weatherapp.service


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService {
    companion object {
        val BASE_URL = "https://api.openweathermap.org/"
        fun getApiImplementation(): WeatherApi {
            return getClient(BASE_URL).create(WeatherApi::class.java)
        }

        fun getClient(baseUrl: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                )
                .build()
        }
    }
}