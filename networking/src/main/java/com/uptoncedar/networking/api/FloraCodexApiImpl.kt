package com.uptoncedar.networking.api

import com.uptoncedar.networking.model.ApiMeta
import com.uptoncedar.networking.model.PaginatedModel
import com.uptoncedar.networking.model.Plant
import retrofit2.Response
import javax.inject.Inject

class FloraCodexApiImpl @Inject constructor(): FloraCodexApi {
    override suspend fun getPlants(query: String): Response<PaginatedModel<List<Plant>>> {
        return Response.success(
            PaginatedModel<List<Plant>>(
                data = emptyList<Plant>(),
                first = "",
                last = "",
                self = "",
                meta = ApiMeta(
                    total = 1
                )
            )
        )
    }
}