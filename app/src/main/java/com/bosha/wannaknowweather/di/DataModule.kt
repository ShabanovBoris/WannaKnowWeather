package com.bosha.wannaknowweather.di

import com.bosha.data.repositoriesImpl.WeatherRepositoryImpl
import com.bosha.domain.repositories.WeatherRepository
import dagger.Binds
import dagger.BindsInstance
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun provideWeatherRepository(repositoryImpl: WeatherRepositoryImpl): WeatherRepository
}