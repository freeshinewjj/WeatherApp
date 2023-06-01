package com.jay.weatherapp.weather.domain.usecase

import com.jay.weatherapp.common.domain.model.weather.Weather
import com.jay.weatherapp.common.domain.repositories.UserPrefRepository
import com.jay.weatherapp.common.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RequestWeatherForLastSearched @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val userPrefRepository: UserPrefRepository
) {
    suspend operator fun invoke(): Weather? {
        val searchParameter = userPrefRepository.lastSearchParameter.first()
        return searchParameter?.let {
            weatherRepository.queryWeather(it)
        }
    }
}