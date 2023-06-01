package com.jay.weatherapp.common.data.api

import com.jay.weatherapp.common.data.api.model.weather.ApiWeather
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    @GET("/data/2.5/weather")
    suspend fun queryWeather(@Query("q") query: String): ApiWeather
    @GET("/data/2.5/weather")
    suspend fun queryWeather(@Query("lat") lat: Double, @Query("lon") lon: Double): ApiWeather
}