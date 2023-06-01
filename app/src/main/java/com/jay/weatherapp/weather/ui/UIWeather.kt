package com.jay.weatherapp.weather.ui

import com.jay.weatherapp.common.domain.model.weather.Weather
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class UIWeather(
    val cityId: Int,
    val cityName: String,
    val weather: String,
    val description: String,
    val temp: Double,
    val icon: String,
    val time: String
) {
    companion object {
        fun fromDomain(weather: Weather): UIWeather {
            return UIWeather(cityId = weather.cityId, cityName = weather.cityName, weather = weather.weather, description = weather.description,
                temp = weather.temp, icon = weather.icon, computeTime(weather.timestamp)
            )
        }

        private fun computeTime(timestamp: Long): String {
            val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
            return  date.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }
    }
}