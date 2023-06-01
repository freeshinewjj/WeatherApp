package com.jay.weatherapp.weather.domain.usecase

import com.jay.weatherapp.common.domain.model.weather.Weather
import com.jay.weatherapp.common.domain.repositories.WeatherRepository
import javax.inject.Inject

class RequestWeatherWithLatLon @Inject constructor(private val repository: WeatherRepository)  {
    suspend operator fun invoke(lat: Double, lon: Double): Weather {
        return repository.queryWeather(lat, lon)
    }
}