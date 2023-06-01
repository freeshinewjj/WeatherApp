package com.jay.weatherapp.common.data.di

import com.jay.weatherapp.common.data.repositories.DataStoreUserPrefRepository
import com.jay.weatherapp.common.data.repositories.OpenWeatherRepository
import com.jay.weatherapp.common.domain.repositories.UserPrefRepository
import com.jay.weatherapp.common.domain.repositories.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {
    @Binds
    @Singleton
    abstract fun bindWeatherRepository(weatherRepository: OpenWeatherRepository): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindUserPrefRepository(userPrefRepository: DataStoreUserPrefRepository): UserPrefRepository
}