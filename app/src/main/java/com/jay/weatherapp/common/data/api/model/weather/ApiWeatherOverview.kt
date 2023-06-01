package com.jay.weatherapp.common.data.api.model.weather

data class ApiWeatherOverview (
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)