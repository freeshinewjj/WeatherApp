package com.jay.weatherapp.weather.ui

import com.jay.weatherapp.common.ui.viewmodel.BaseViewModel
import com.jay.weatherapp.common.ui.viewmodel.Event
import com.jay.weatherapp.weather.domain.usecase.RequestWeatherForCurrentLocation
import com.jay.weatherapp.weather.domain.usecase.RequestWeatherForLastSearched
import com.jay.weatherapp.weather.domain.usecase.RequestWeatherWithSearchString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val requestWeatherForCurrentLocation: RequestWeatherForCurrentLocation,
    private val requestWeatherForLastSearched: RequestWeatherForLastSearched,
    private val requestWeatherWithSearchString: RequestWeatherWithSearchString
): BaseViewModel<HomeFragmentViewState, HomeFragmentIntent>() {
    private var _state = MutableStateFlow<HomeFragmentViewState>(HomeFragmentViewState())
    override val state: StateFlow<HomeFragmentViewState>
        get() = _state.asStateFlow()

    override fun handleIntent(intent: HomeFragmentIntent) {
        val coroutineExceptionHandler = CoroutineExceptionHandler {_, throwable ->
            onFailure(throwable)
        }
        launch(coroutineExceptionHandler) {
            when(intent) {
                is HomeFragmentIntent.RequestWeatherForCurrentLocation -> handleRequestWeatherForCurrentCity()
                is HomeFragmentIntent.RequestWeatherForLastSearchedCity -> handleRequestWeatherForLastSearchedCity()
                is HomeFragmentIntent.RequestWeatherWithSearchString -> handleRequestWeatherWithSearchString(intent.query)
            }
        }
    }

    private suspend fun handleRequestWeatherForCurrentCity() {
        _state.emit(_state.value.copy(isLoading = true))
        val weather = requestWeatherForCurrentLocation()
        _state.emit(_state.value.copy(isLoading = false, weather = UIWeather.fromDomain(weather)))
    }

    private suspend fun handleRequestWeatherForLastSearchedCity() {
        _state.emit(_state.value.copy(isLoading = true))
        val weather = requestWeatherForLastSearched()
        _state.emit(_state.value.copy(isLoading = false, weather = weather?.let { UIWeather.fromDomain(it)}))
    }

    private suspend fun handleRequestWeatherWithSearchString(query: String) {
        _state.emit(_state.value.copy(isLoading = true))
        val weather = requestWeatherWithSearchString(query)
        _state.emit(_state.value.copy(isLoading = false, weather = UIWeather.fromDomain(weather)))
    }

    private fun onFailure(throwable: Throwable) {
        launch {
            _state.emit(_state.value.copy(isLoading = false, failure = Event(throwable)))
        }
    }
}