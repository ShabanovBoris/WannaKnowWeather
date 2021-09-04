package com.bosha.wannaknowweather.di

import com.bosha.domain.repositories.WeatherRepository
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.domain.usecases.HourlyWeatherForecastUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class UseCaseModule {

    @Provides
    fun provideCurrentWeatherUseCase(repository: WeatherRepository): CurrentWeatherUseCase =
        CurrentWeatherUseCase(repository)

    @Provides
    fun provideHourlyWeatherForecastUseCase(repository: WeatherRepository): HourlyWeatherForecastUseCase =
        HourlyWeatherForecastUseCase(repository)


    //todo stub
    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Main

}