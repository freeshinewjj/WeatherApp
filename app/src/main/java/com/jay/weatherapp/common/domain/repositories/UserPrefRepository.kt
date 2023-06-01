package com.jay.weatherapp.common.domain.repositories

import com.jay.weatherapp.common.domain.model.weather.SearchParameter
import kotlinx.coroutines.flow.Flow

interface UserPrefRepository {
    val lastSearchParameter: Flow<SearchParameter?>
    suspend fun saveSearchParameter(searchParameter: SearchParameter)
}