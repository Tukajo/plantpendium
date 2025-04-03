package com.uptoncedar.plant.details.domain

import com.uptoncedar.common.model.PlantDetails
import com.uptoncedar.networking.api.FloraCodexApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import android.util.LruCache


interface GetPlantByIdUseCase {
    suspend operator fun invoke(id: String): Result<PlantDetails>
}

@Singleton
class GetPlantByIdUseCaseImpl @Inject constructor(
    private val floraCodexApi: FloraCodexApi
) : GetPlantByIdUseCase {

    private val cache = LruCache<String, PlantDetails>(50)

    override suspend operator fun invoke(id: String): Result<PlantDetails> {
        val cachedPlant = cache.get(id)
        if (cachedPlant != null) {
            return Result.success(cachedPlant)
        }

        return withContext(Dispatchers.IO) {
            try {
                val res = floraCodexApi.getPlantById(id = id)
                if (res.isSuccessful) {
                    val plantDetails = res.body()
                    if (plantDetails != null) {
                        cache.put(id, plantDetails)
                        Result.success(plantDetails)
                    } else {
                        Result.failure(PlantDetailsError.PlantNotFound)
                    }
                } else {
                    Result.failure(PlantDetailsError.NetworkError())
                }
            } catch (e: IOException) {
                Result.failure(PlantDetailsError.NetworkError(e))
            } catch (e: Exception) {
                Result.failure(PlantDetailsError.UnknownError(e))
            }
        }
    }
}