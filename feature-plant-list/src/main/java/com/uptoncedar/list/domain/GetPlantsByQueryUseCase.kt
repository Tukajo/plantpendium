package com.uptoncedar.list.domain

import com.uptoncedar.common.model.PlantListEntry
import com.uptoncedar.networking.api.FloraCodexApi
import javax.inject.Inject

interface GetPlantsByQueryUseCase {
    suspend operator fun invoke(query: String): List<PlantListEntry>
}

class GetPlantsByQueryUseCaseImpl @Inject constructor(
    private val floraCodexApi: FloraCodexApi
) : GetPlantsByQueryUseCase {
    override suspend operator fun invoke(query: String): List<PlantListEntry> {
        val res = floraCodexApi.getPlants(query = query)
        return when (res.isSuccessful) {
            true -> res.body()?.data ?: emptyList()
            else -> {
                throw Exception("Error, unable to retrieve result")
            }
        }
    }
}