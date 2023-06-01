package com.jay.weatherapp.common.data.repositories

import com.jay.weatherapp.common.data.api.WeatherApi
import com.jay.weatherapp.common.data.api.mappers.ApiWeatherMapper
import com.jay.weatherapp.common.data.di.IoDispatcher
import com.jay.weatherapp.common.domain.model.weather.SearchParameter
import com.jay.weatherapp.common.domain.model.weather.Weather
import com.jay.weatherapp.common.domain.repositories.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OpenWeatherRepository @Inject constructor(
    private val api: WeatherApi,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val apiWeatherMapper: ApiWeatherMapper
): WeatherRepository {
    override suspend fun queryWeather(
        searchParameter: SearchParameter
    ): Weather =
        withContext(ioDispatcher) {
            apiWeatherMapper.mapToDomain(api.queryWeather(searchParameter.toSearchString()))
        }

    override suspend fun queryWeather(lat: Double, lon: Double): Weather
        = withContext(ioDispatcher) {
            apiWeatherMapper.mapToDomain(api.queryWeather(lat, lon))
    }
}