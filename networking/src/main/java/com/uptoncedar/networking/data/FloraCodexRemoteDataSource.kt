package com.uptoncedar.networking.data

import com.uptoncedar.common.model.PlantListEntry
import com.uptoncedar.networking.api.FloraCodexApi
import com.uptoncedar.networking.model.PaginatedModel
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

interface FloraCodexRemoteDataSource {
    suspend fun getPlantsByQuery(query: String): Response<PaginatedModel<List<PlantListEntry>>>
}

@Singleton
class FloraCodexRemoteDataSourceImpl @Inject constructor(
    private val floraCodexApi: FloraCodexApi
) : FloraCodexRemoteDataSource {

    override suspend fun getPlantsByQuery(query: String): Response<PaginatedModel<List<PlantListEntry>>> {
        return floraCodexApi.getPlants(query = query)
    }
}