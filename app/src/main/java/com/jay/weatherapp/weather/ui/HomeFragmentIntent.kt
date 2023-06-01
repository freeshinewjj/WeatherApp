package com.jay.weatherapp.weather.ui

sealed class HomeFragmentIntent {
    object RequestWeatherForCurrentLocation: HomeFragmentIntent()
    object RequestWeatherForLastSearchedCity: HomeFragmentIntent()
    class RequestWeatherWithSearchString(val query: String): HomeFragmentIntent()
}
