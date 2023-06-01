package com.jay.weatherapp.common.domain.repositories

import com.jay.weatherapp.common.domain.model.weather.SearchParameter
import com.jay.weatherapp.common.domain.model.weather.Weather

interface WeatherRepository {
    suspend fun queryWeather(searchParameter: SearchParameter): Weather
    suspend fun queryWeather(lat: Double, lon: Double): Weather
}