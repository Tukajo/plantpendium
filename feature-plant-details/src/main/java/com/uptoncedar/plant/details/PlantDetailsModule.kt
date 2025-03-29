package com.uptoncedar.plant.details

import com.uptoncedar.plant.details.domain.GetPlantByIdUseCase
import com.uptoncedar.plant.details.domain.GetPlantByIdUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlantDetailsModule {

    @Binds
    @Singleton
    abstract fun binds(getPlantByIdUseCase: GetPlantByIdUseCaseImpl): GetPlantByIdUseCase
}