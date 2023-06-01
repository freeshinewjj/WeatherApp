package com.jay.weatherapp.common.data.api.model.weather

import com.google.gson.annotations.SerializedName

data class ApiWeather(
    val id: Int,
    val name: String,
    @SerializedName("weather")
    val overviews: List<ApiWeatherOverview>,
    val main: ApiWeatherMain,
    val visibility: Int,
    @SerializedName("dt")
    val timestamp: Long
)