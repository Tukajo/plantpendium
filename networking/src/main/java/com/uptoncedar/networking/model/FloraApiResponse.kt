package com.uptoncedar.networking.model

data class PaginatedModel<T>(
    val data: T,
    val self: String,
    val first: String,
    val last: String,
    val meta: ApiMeta
)

data class ApiMeta(
    val total: Int
)