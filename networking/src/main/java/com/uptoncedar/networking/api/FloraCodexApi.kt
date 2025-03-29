package com.uptoncedar.networking.api

import com.uptoncedar.networking.model.PaginatedModel
import com.uptoncedar.networking.model.PlantDetails
import com.uptoncedar.networking.model.PlantListEntry
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FloraCodexApi {
    @GET("plants")
    suspend fun getPlants(@Query("q") query: String): Response<PaginatedModel<List<PlantListEntry>>>

    @GET("plants/{id}")
    suspend fun getPlantById(@Path("id") id: String): Response<PlantDetails>
}