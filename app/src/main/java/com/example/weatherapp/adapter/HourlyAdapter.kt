package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.model.ForecastBody
import kotlinx.android.synthetic.main.hourly_card.view.*
import java.text.SimpleDateFormat
import java.util.*

class HourlyAdapter(private var hourlyList: List<ForecastBody>) :
    RecyclerView.Adapter<HourlyAdapter.HourlyListDesign>() {

    class HourlyListDesign(view: View) : RecyclerView.ViewHolder(view) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyListDesign {
        val design =
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_card, parent, false)
        return HourlyListDesign(design)
    }
    override fun onBindViewHolder(holder: HourlyListDesign, position: Int) {
        val day = hourlyList[position]

        Glide.with(holder.itemView.context).load("https://openweathermap.org/img/w/${day.weather[0].icon}.png")
            .into(holder.itemView.imageView_hourly_icon)

        holder.itemView.textView_hourly_temp.text = "${day.main.temp.toInt()}°C"

        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Girdi formatı
        val outputFormat = SimpleDateFormat("EEEE HH:mm", Locale.getDefault())
        val inputDate = inputFormat.parse(day.dt_txt)
        val outputDate = outputFormat.format(inputDate)

        holder.itemView.textView_hourly_time.text = outputDate
    }

    override fun getItemCount(): Int {
        return hourlyList.size
    }
}
