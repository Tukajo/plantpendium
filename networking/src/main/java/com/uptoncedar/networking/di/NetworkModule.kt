package com.uptoncedar.networking.di

import com.uptoncedar.networking.RetrofitInstance
import com.uptoncedar.networking.api.FloraCodexApi
import com.uptoncedar.networking.data.FloraCodexRemoteDataSource
import com.uptoncedar.networking.data.FloraCodexRemoteDataSourceImpl
import com.uptoncedar.networking.data.FloraCodexRepositoryImpl
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Module
import dagger.Provides

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideFloraCodexApi(): FloraCodexApi {
        return RetrofitInstance.api
    }

    @Provides
    fun provideFloraCodexRemoteDataSource(floraCodexApi: FloraCodexApi): FloraCodexRemoteDataSource {
        return FloraCodexRemoteDataSourceImpl(floraCodexApi)
    }

    @Provides
    fun provideFloraCodexRepository(floraCodexRemoteDataSource: FloraCodexRemoteDataSource): FloraCodexRepositoryImpl {
        return FloraCodexRepositoryImpl(floraCodexRemoteDataSource)
    }
}