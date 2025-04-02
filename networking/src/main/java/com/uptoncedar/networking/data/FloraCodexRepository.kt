package com.uptoncedar.networking.data

import com.uptoncedar.common.model.PlantListEntry
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject
import javax.inject.Singleton


interface FloraCodexRepository {
    suspend fun getPlantsByQuery(
        query: String,
        refresh: Boolean = false
    ): Result<List<PlantListEntry>>
}


@Singleton
class FloraCodexRepositoryImpl @Inject constructor(
    private val floraCodexRemoteDataSource: FloraCodexRemoteDataSource
) : FloraCodexRepository {
    private val plantListCache = mutableMapOf<String, List<PlantListEntry>>()
    private val cacheMutex = Mutex()

    override suspend fun getPlantsByQuery(
        query: String,
        refresh: Boolean
    ): Result<List<PlantListEntry>> {
        return try {
            cacheMutex.lock()
            val cachedPlants = plantListCache[query]
            return when {
                !refresh && cachedPlants != null -> {
                    Result.Success(cachedPlants)
                }
                else -> {
                    fetchByQuery(query)
                }
            }

        } finally {
            cacheMutex.unlock()
        }
    }

    private suspend fun fetchByQuery(query: String): Result<List<PlantListEntry>> {
        val remoteResponse = floraCodexRemoteDataSource.getPlantsByQuery(query)
        return if (remoteResponse.isSuccessful) {
            when (val body = remoteResponse.body()) {
                null -> Result.Failure(Exception("Empty response body"))
                else -> {
                    plantListCache[query] = body.data
                    Result.Success(body.data)
                }
            }
        } else {
            Result.Failure(Exception("Network error: ${remoteResponse.code()}"))
        }
    }
}