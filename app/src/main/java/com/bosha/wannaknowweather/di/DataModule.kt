package com.bosha.wannaknowweather.di

import com.bosha.data.repositoriesImpl.WeatherRepositoryImpl
import com.bosha.domain.repositories.WeatherRepository
import com.bosha.wannaknowweather.di.scopes.ScreenScope
import dagger.Binds
import dagger.BindsInstance
import dagger.Module

@Module
interface DataModule {

    @ScreenScope
    @Binds
    fun provideWeatherRepository(repositoryImpl: WeatherRepositoryImpl): WeatherRepository
}