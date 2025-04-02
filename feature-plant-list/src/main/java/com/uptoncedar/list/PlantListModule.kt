package com.uptoncedar.list


import com.uptoncedar.networking.data.FloraCodexRepository
import com.uptoncedar.networking.data.FloraCodexRepositoryImpl
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
    abstract fun bindsFloraCodexRepository(floraCodexRepository: FloraCodexRepositoryImpl): FloraCodexRepository

}