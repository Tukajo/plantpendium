package com.uptoncedar.networking

import com.uptoncedar.networking.api.FloraCodexApi
import com.uptoncedar.networking.interceptors.ApiKeyInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val FLORA_API_BASE_URL = "https://api.floracodex.com/v1/"
    val api: FloraCodexApi by lazy {

        val apiKey =
            "***REMOVED***" // TODO This traffic would either need to be proxied through a secure server, or built in such a way that we don't expose the key.
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(apiKey))
            .build()

        Retrofit.Builder()
            .baseUrl(FLORA_API_BASE_URL)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FloraCodexApi::class.java)
    }
}