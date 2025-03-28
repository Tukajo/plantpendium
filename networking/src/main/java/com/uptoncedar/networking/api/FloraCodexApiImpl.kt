package com.uptoncedar.networking.api

import com.uptoncedar.networking.model.Plant
import retrofit2.Response
import javax.inject.Inject

class FloraCodexApiImpl @Inject constructor(): FloraCodexApi {
    override suspend fun getPlants(query: String): Response<List<Plant>> {
        return Response.success(emptyList())
    }
}