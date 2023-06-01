package com.jay.weatherapp.common.data.di

import com.jay.weatherapp.common.data.api.ApiConstants
import com.jay.weatherapp.common.data.api.WeatherApi
import com.jay.weatherapp.common.data.api.interceptors.RequestInterceptor
import com.jay.weatherapp.common.data.api.interceptors.NetworkStatusInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherApiModule {
    @Provides
    @Singleton
    fun provideCalorieApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(ApiConstants.SERVER_BASE_URL)
            .client(client).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        networkStatusInterceptor: NetworkStatusInterceptor,
        authenticationInterceptor: RequestInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(ApiConstants.DEFAULT_CONN_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(ApiConstants.DEFAULT_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(networkStatusInterceptor)
            .addInterceptor(authenticationInterceptor)
            .build()
    }
}