package com.example.weatherapp.model

data class Forecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastBody>,
    val message: Int
)

data class City(
    val coord: ForecastCoord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

data class ForecastWind(
    val deg: Int,
    val gust: Double,
    val speed: Double
)

data class ForecastClouds(
    val all: Int
)

data class ForecastCoord(
    val lat: Double,
    val lon: Double
)

data class ForecastBody(
    val clouds: ForecastClouds,
    val dt: Int,
    val dt_txt: String,
    val main: ForecastMain,
    val pop: Double,
    val rain: ForecastRain,
    val sys: ForecastSys,
    val visibility: Int,
    val weather: List<ForecastWeather>,
    val wind: ForecastWind
)

data class ForecastMain(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class ForecastRain(
    val `3h`: Double
)

data class ForecastSys(
    val pod: String
)

data class ForecastWeather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)