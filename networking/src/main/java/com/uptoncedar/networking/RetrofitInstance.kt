package com.uptoncedar.networking

import com.uptoncedar.networking.api.FloraCodexApi
import com.uptoncedar.networking.interceptors.ApiKeyInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val FLORA_API_BASE_URL = "https://api.floracodex.com/v1/"
    val api: FloraCodexApi by lazy {

        val floraApiKey = BuildConfig.FLORA_API_KEY
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(ApiKeyInterceptor(floraApiKey))
            .build()

        Retrofit.Builder()
            .baseUrl(FLORA_API_BASE_URL)
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FloraCodexApi::class.java)
    }
}