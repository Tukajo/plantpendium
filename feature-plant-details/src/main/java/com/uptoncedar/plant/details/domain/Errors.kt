package com.uptoncedar.plant.details.domain

sealed class PlantDetailsError : Exception() {
    data object PlantNotFound : PlantDetailsError()
    data class NetworkError(override val cause: Throwable? = null) : PlantDetailsError()
    data class UnknownError(override val cause: Throwable? = null) : PlantDetailsError()
}