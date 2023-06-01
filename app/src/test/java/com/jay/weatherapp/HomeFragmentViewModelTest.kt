package com.jay.weatherapp

import com.jay.weatherapp.common.domain.exceptions.NoLocationPermissionException
import com.jay.weatherapp.common.domain.model.weather.Weather
import com.jay.weatherapp.weather.domain.usecase.RequestWeatherForCurrentLocation
import com.jay.weatherapp.weather.domain.usecase.RequestWeatherForLastSearched
import com.jay.weatherapp.weather.domain.usecase.RequestWeatherWithSearchString
import com.jay.weatherapp.weather.ui.HomeFragmentIntent
import com.jay.weatherapp.weather.ui.HomeFragmentViewModel
import com.jay.weatherapp.weather.ui.UIWeather
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.junit.Assert.*

class HomeFragmentViewModelTest {
    @get:Rule
    val mainDispatcherRule = CoroutineTestRule()

    @MockK
    lateinit var requestWeatherForCurrentLocation: RequestWeatherForCurrentLocation

    @MockK
    lateinit var requestWeatherForLastSearched: RequestWeatherForLastSearched

    @MockK
    lateinit var requestWeatherWithSearchString: RequestWeatherWithSearchString

    @Before
    fun setup() = MockKAnnotations.init(this)

    @Test
    fun testRequestWeatherForCurrentLocation() = runTest {
        val weather = Weather(
            cityId = 1,
            cityName = "Irvine",
            weather = "Sunny",
            description = "Sunny",
            temp = 65.0,
            icon = "http://icon.com/icon1",
            timestamp = 1685653591L
        )
        coEvery { requestWeatherForCurrentLocation() } returns weather
        val viewModel = HomeFragmentViewModel(
            requestWeatherForCurrentLocation, requestWeatherForLastSearched, requestWeatherWithSearchString
        )

        viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherForCurrentLocation)
        advanceUntilIdle()
        assertEquals(viewModel.state.value.isLoading, false)
        assertEquals(viewModel.state.value.weather, UIWeather.fromDomain(weather))
        assertEquals(viewModel.state.value.failure, null)
        coVerify (exactly = 1) { requestWeatherForCurrentLocation() }
    }

    @Test
    fun testRequestWeatherForLastSearched() = runTest {
        val weather = Weather(
            cityId = 1,
            cityName = "Irvine",
            weather = "Sunny",
            description = "Sunny",
            temp = 65.0,
            icon = "http://icon.com/icon1",
            timestamp = 1685653591L
        )
        coEvery { requestWeatherForLastSearched() } returns weather
        val viewModel = HomeFragmentViewModel(
            requestWeatherForCurrentLocation, requestWeatherForLastSearched, requestWeatherWithSearchString
        )

        viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherForLastSearchedCity)
        advanceUntilIdle()
        assertEquals(viewModel.state.value.isLoading, false)
        assertEquals(viewModel.state.value.weather, UIWeather.fromDomain(weather))
        assertEquals(viewModel.state.value.failure, null)
        coVerify(exactly = 1) { requestWeatherForLastSearched() }
    }

    @Test
    fun testRequestWeatherWithSearchString() = runTest {
        val weather = Weather(
            cityId = 1,
            cityName = "Irvine",
            weather = "Sunny",
            description = "Sunny",
            temp = 65.0,
            icon = "http://icon.com/icon1",
            timestamp = 1685653591L
        )
        val query = "Irvine,US"
        coEvery { requestWeatherWithSearchString(query) } returns weather
        val viewModel = HomeFragmentViewModel(
            requestWeatherForCurrentLocation, requestWeatherForLastSearched, requestWeatherWithSearchString
        )

        viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherWithSearchString(query))
        advanceUntilIdle()
        assertEquals(viewModel.state.value.isLoading, false)
        assertEquals(viewModel.state.value.weather, UIWeather.fromDomain(weather))
        assertEquals(viewModel.state.value.failure, null)
        coVerify(exactly = 1) { requestWeatherWithSearchString(query) }
    }

    @Test
    fun testFailure() = runTest {
        coEvery { requestWeatherForCurrentLocation() } throws NoLocationPermissionException("no location permission")
        val viewModel = HomeFragmentViewModel(
            requestWeatherForCurrentLocation, requestWeatherForLastSearched, requestWeatherWithSearchString
        )

        viewModel.dispatchIntent(HomeFragmentIntent.RequestWeatherForCurrentLocation)
        advanceUntilIdle()
        assertEquals(viewModel.state.value.isLoading, false)
        assertTrue(viewModel.state.value.failure !=null)
        assertTrue(viewModel.state.value.failure?.getContentIfNotHandled()!! is NoLocationPermissionException)
        coVerify(exactly = 1) { requestWeatherForCurrentLocation() }
    }
}