package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.weatherapp.adapter.HourlyAdapter
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.model.ForecastBody
import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.service.WeatherService
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 123123
    private val apiKey = "75faa8083541a6689f4998dab66234a4"
    private val units = "metric"

    private val LOCATION_UPDATE_INTERVAL = 5 * 60 * 1000 // 5 minutes
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var hourlyList: MutableList<ForecastBody> = mutableListOf()
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        askForClothingAdvice.setOnClickListener {
            val intent = Intent(this, ClothingActivity::class.java)
            intent.putExtra("degree", temp.text.toString())
            intent.putExtra("status", status.text.toString())
            intent.putExtra("humidity", humidity.text.toString())
            intent.putExtra("wind", wind.text.toString())
            intent.putExtra("rain", rain.text.toString())
            startActivity(intent)
        }
        convertTime()
        permissionCheck()
    }
    private fun getCurrentLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                fetchWeatherMap(location.latitude, location.longitude)
                fetchForecastMap(location.latitude, location.longitude)
            } else {
                Log.e("alppppp", "Location not found")
            }
        }
    }
    private fun startLocationUpdates() {
        locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(LOCATION_UPDATE_INTERVAL.toLong())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Konumunuzu kullanarak hava durumunu yeniden çekin
                    fetchWeatherMap(location.latitude, location.longitude)
                    fetchForecastMap(location.latitude, location.longitude)
                }
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient?.requestLocationUpdates(locationRequest, locationCallback, null)
    }
    fun fetchWeatherMap(lat: Double, lon: Double) {
        WeatherService.getApiImplementation().getData(lat,lon,apiKey, units).enqueue(object : Callback<WeatherModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                val resBody = response.body()

                if (resBody != null) {
                    val dateSunrise = Date(resBody.sys.sunrise * 1000L)
                    val dateSunset = Date(resBody.sys.sunset * 1000L)
                    val format = SimpleDateFormat("hh:mm aa", Locale.getDefault())
                    addressText.text = resBody.name
                    country.text = resBody.sys.country
                    temp.text = "${resBody.main.temp.toInt()}°C"
                    status.text = resBody.weather[0].main
                    wind.text = "${resBody.wind.speed}m/s"
                    pressure.text = "${resBody.main.pressure} hPa"
                    humidity.text = "%${resBody.main.humidity}"
                    sunset.text = format.format(dateSunset)
                    sunrise.text = format.format(dateSunrise)
                    rain.text = if (resBody.rain == null) {
                        "%0"
                    } else {
                        val rainPercentage = "%.0f".format(BigDecimal(resBody.rain.toString()) * BigDecimal.valueOf(100))
                        "%${rainPercentage}"
                    }
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Log.e("alppppp", "Failed::" + (t))
            }
        })
    }
    fun fetchForecastMap(lat: Double, lon: Double) {
        WeatherService.getApiImplementation().getForecast(lat,lon,apiKey, units).enqueue(object : Callback<Forecast> {
            override fun onResponse(call: Call<Forecast>, response: Response<Forecast>) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    hourlyList.addAll(resBody?.list!!)
                    hourlyAdapter.notifyDataSetChanged()
                }

            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                Log.e("alppppp", "Failed::" + (t))
            }
        })
    }
    private fun convertTime() {

        val currentTime = Calendar.getInstance()
        val sunsetTime = Calendar.getInstance()

        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val currentDateString = format.format(currentTime.time)
        val currentTimeParsed = format.parse(currentDateString)

        val sunsetDateString = sunset.text.toString()
        val sunsetTimeParsed = format.parse(sunsetDateString)

        sunsetTime.time = sunsetTimeParsed

        if (currentTime.get(Calendar.HOUR_OF_DAY) > sunsetTime.get(Calendar.HOUR_OF_DAY)) {
            main.setBackgroundResource(R.drawable.daytime_bg)
        } else {
            main.setBackgroundResource(R.drawable.night_bg)
        }

    }
    private fun permissionCheck() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Kullanıcı zaten konum izni vermişse, konum bilgisi alinacak
            startLocationUpdates()
            getCurrentLocation()
        } else {
            // İzin isteği göster
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView_forecast)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        hourlyAdapter = HourlyAdapter(hourlyList)
        recyclerView.adapter = hourlyAdapter
    }
}