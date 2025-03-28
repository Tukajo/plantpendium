package com.uptoncedar.list.di

import com.uptoncedar.networking.api.FloraCodexApi
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
    abstract fun bind(floraCodexApi: FloraCodexApi): FloraCodexApi
}