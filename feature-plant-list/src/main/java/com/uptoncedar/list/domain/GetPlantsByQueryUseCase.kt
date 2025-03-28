package com.uptoncedar.list.domain

import com.uptoncedar.networking.api.FloraCodexApi
import com.uptoncedar.networking.model.Plant
import javax.inject.Inject

interface GetPlantsByQueryUseCase {
    suspend operator fun invoke(query: String): List<Plant>
}

class GetPlantsByQueryUseCaseImpl @Inject constructor(
    private val floraCodexApi: FloraCodexApi
): GetPlantsByQueryUseCase {
    override suspend operator fun invoke(query: String): List<Plant> {
        val res = floraCodexApi.getPlants(query)
        return when (res.isSuccessful) {
            true -> res.body()?.data ?: emptyList()
            else -> {
                throw Exception("Error, unable to retrieve result")
            }
        }
    }
}