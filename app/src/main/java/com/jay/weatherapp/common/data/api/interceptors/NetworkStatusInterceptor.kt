package com.jay.weatherapp.common.data.api.interceptors

import com.jay.weatherapp.common.domain.exceptions.NetworkUnavailableException
import com.jay.weatherapp.common.util.ConnectionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NetworkStatusInterceptor @Inject constructor(private val connectionManager: ConnectionManager):
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (connectionManager.isConnected) {
            chain.proceed(chain.request())
        } else {
            throw NetworkUnavailableException("Network connectivity unavailable")
        }
    }
}