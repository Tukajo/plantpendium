package com.uptoncedar.list

import com.uptoncedar.list.domain.GetPlantsByQueryUseCase
import com.uptoncedar.list.domain.GetPlantsByQueryUseCaseImpl
import com.uptoncedar.networking.api.FloraCodexApi
import com.uptoncedar.networking.api.FloraCodexApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlantListModule {

    @Binds
    @Singleton
    abstract fun bindGetPlantsByQueryUseCase(getPlantsByQueryUseCase: GetPlantsByQueryUseCaseImpl): GetPlantsByQueryUseCase
}