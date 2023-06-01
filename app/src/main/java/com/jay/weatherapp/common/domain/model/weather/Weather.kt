package com.jay.weatherapp.common.domain.model.weather

data class Weather(
    val cityId: Int,
    val cityName: String,
    val weather: String,
    val description: String,
    val temp: Double,
    val icon: String,
    val timestamp: Long
)
