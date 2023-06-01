package com.jay.weatherapp.common.data.api.model.weather

import com.google.gson.annotations.SerializedName

data class ApiWeatherMain(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)
