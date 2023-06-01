package com.jay.weatherapp.weather.ui

import com.jay.weatherapp.common.ui.viewmodel.Event

data class HomeFragmentViewState(
    val isLoading: Boolean = false,
    val weather: UIWeather? = null,
    val failure: Event<Throwable>? = null
)
