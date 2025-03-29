package com.uptoncedar.networking.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalReq = chain.request()
        val originalUrl = originalReq.url
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("key", apiKey)
            .build()
        val newReq = originalReq.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newReq)
    }
}