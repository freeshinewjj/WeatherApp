package com.jay.weatherapp.common.data.api.interceptors

import com.jay.weatherapp.common.data.api.ApiConstants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestInterceptor @Inject constructor(): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val updatedUrl = originalHttpUrl.newBuilder().addQueryParameter("units","imperial").addQueryParameter(ApiConstants.API_KEY_NAME, ApiConstants.API_KEY).build()
        val requestBuilder = original.newBuilder().url(updatedUrl)
        val updatedRequest = requestBuilder.build()
        return chain.proceed(updatedRequest)
    }
}