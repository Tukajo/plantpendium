package com.uptoncedar.networking.api

import com.uptoncedar.networking.model.Plant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FloraCodexApi {
    @GET("plants")
    suspend fun getPlants(@Query("q") query: String): Response<List<Plant>>
}