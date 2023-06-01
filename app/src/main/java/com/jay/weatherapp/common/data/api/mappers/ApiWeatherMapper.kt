package com.jay.weatherapp.common.data.api.mappers

import com.jay.weatherapp.common.data.api.ApiConstants
import com.jay.weatherapp.common.data.api.model.weather.ApiWeather
import com.jay.weatherapp.common.domain.exceptions.ServerDataParseError
import com.jay.weatherapp.common.domain.model.weather.Weather
import javax.inject.Inject

class ApiWeatherMapper @Inject constructor(): ApiMapper<ApiWeather, Weather> {
    override fun mapToDomain(entity: ApiWeather): Weather {
        val apiWeatherOverviewFirst = entity.overviews.firstOrNull()
        if (apiWeatherOverviewFirst != null) {
            return Weather(entity.id, entity.name, apiWeatherOverviewFirst.main, apiWeatherOverviewFirst.description, entity.main.temp, computeIconUrl(apiWeatherOverviewFirst.icon), entity.timestamp)
        }

        throw ServerDataParseError("Failed to pass server data.")
    }

    private fun computeIconUrl(icon: String): String {
        val base = ApiConstants.ICON_BASE_URL
        return if (base.endsWith('/')) {
            "$base$icon@2x.png"
        } else {
            "$base/$icon@2x.png"
        }
    }
}