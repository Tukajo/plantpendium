package com.uptoncedar.networking.di

import com.uptoncedar.networking.RetrofitInstance
import com.uptoncedar.networking.api.FloraCodexApi
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
}