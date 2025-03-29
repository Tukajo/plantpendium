package com.uptoncedar.plant.details.domain

import com.uptoncedar.networking.api.FloraCodexApi
import com.uptoncedar.networking.model.PlantDetails
import javax.inject.Inject

interface GetPlantByIdUseCase {
    suspend operator fun invoke(id: String): PlantDetails
}

class GetPlantByIdUseCaseImpl @Inject constructor(
    private val floraCodexApi: FloraCodexApi
) : GetPlantByIdUseCase {
    override suspend operator fun invoke(id: String): PlantDetails {
        val res = floraCodexApi.getPlantById(id = id)
        return when (res.isSuccessful) {
            true -> res.body() ?: throw Exception("Plant details not found")
            else -> {
                throw Exception("Error, unable to retrieve plant details")
            }
        }
    }
}