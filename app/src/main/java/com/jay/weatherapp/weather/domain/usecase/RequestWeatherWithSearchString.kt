package com.jay.weatherapp.weather.domain.usecase

import com.jay.weatherapp.common.domain.model.weather.SearchParameter
import com.jay.weatherapp.common.domain.model.weather.Weather
import com.jay.weatherapp.common.domain.repositories.UserPrefRepository
import com.jay.weatherapp.common.domain.repositories.WeatherRepository
import javax.inject.Inject

class RequestWeatherWithSearchString @Inject constructor(
    private val repository: WeatherRepository,
    private val userPrefRepository: UserPrefRepository
    ) {
    suspend operator fun invoke(query: String): Weather {
        val parts = query.trim().split(',')
        val name = parts[0]
        var state: String? = null
        var country: String? = null
        if (parts.size >= 2)
            state = parts[1]
        if (parts.size >= 3)
            country = parts[2]

        val searchParameter = SearchParameter(name, state, country)
        val weather = repository.queryWeather(searchParameter)
        userPrefRepository.saveSearchParameter(searchParameter)
        return weather
    }
}