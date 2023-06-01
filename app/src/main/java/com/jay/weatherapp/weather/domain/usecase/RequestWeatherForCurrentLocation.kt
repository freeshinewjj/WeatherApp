package com.jay.weatherapp.weather.domain.usecase

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.jay.weatherapp.common.data.di.IoDispatcher
import com.jay.weatherapp.common.domain.exceptions.NoLocationPermissionException
import com.jay.weatherapp.common.domain.model.weather.Weather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RequestWeatherForCurrentLocation @Inject constructor(
    private val requestWeatherWithLatLon: RequestWeatherWithLatLon,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    suspend operator fun invoke(): Weather =
         try {
             withContext(ioDispatcher) {
                 val location = fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_LOW_POWER, null).await()
                 requestWeatherWithLatLon(location.latitude, location.longitude)
             }
         } catch (secExp: SecurityException) {
             throw NoLocationPermissionException("Location permission is required.")
         }
}