package com.uptoncedar.list.domain

import com.uptoncedar.common.model.PlantListEntry
import com.uptoncedar.networking.api.FloraCodexApi
import javax.inject.Inject
import javax.inject.Singleton

interface GetPlantsByQueryUseCase {
    suspend operator fun invoke(query: String): List<PlantListEntry>
}

@Singleton
class GetPlantsByQueryUseCaseImpl @Inject constructor(
    private val floraCodexApi: FloraCodexApi
) : GetPlantsByQueryUseCase {

    private val cache = mutableMapOf<String, List<PlantListEntry>>()

    override suspend operator fun invoke(query: String): List<PlantListEntry> {
        return cache[query] ?: run {
            val res = floraCodexApi.getPlants(query = query)
            when (res.isSuccessful) {
                true -> {
                    val plantList = res.body()?.data ?: emptyList()
                    cache[query] = plantList
                    plantList
                }
                else -> {
                    throw Exception("Error, unable to retrieve result")
                }
            }
        }
    }
}