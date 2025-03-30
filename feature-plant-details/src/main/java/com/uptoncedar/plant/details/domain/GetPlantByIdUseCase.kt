package com.uptoncedar.plant.details.domain

import com.uptoncedar.common.model.PlantDetails
import com.uptoncedar.networking.api.FloraCodexApi
import javax.inject.Inject
import javax.inject.Singleton

interface GetPlantByIdUseCase {
    suspend operator fun invoke(id: String): PlantDetails
}

@Singleton
class GetPlantByIdUseCaseImpl @Inject constructor(
    private val floraCodexApi: FloraCodexApi
) : GetPlantByIdUseCase {

    private val cache = mutableMapOf<String, PlantDetails>()

    override suspend operator fun invoke(id: String): PlantDetails {
        return cache[id] ?: run {
            val res = floraCodexApi.getPlantById(id = id)
            when (res.isSuccessful) {
                true -> {
                    val plantDetails = res.body() ?: throw Exception("Plant details not found")
                    cache[id] = plantDetails
                    plantDetails
                }
                else -> {
                    throw Exception("Error, unable to retrieve plant details")
                }
            }
        }
    }
}