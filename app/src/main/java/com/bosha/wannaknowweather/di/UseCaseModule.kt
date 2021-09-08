package com.bosha.wannaknowweather.di

import com.bosha.domain.repositories.WeatherRepository
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.domain.usecases.DailyWeatherForecastUseCase
import com.bosha.domain.usecases.HourlyWeatherForecastUseCase
import com.bosha.wannaknowweather.di.scopes.ScreenScope
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class UseCaseModule {
    @ScreenScope
    @Provides
    fun provideCurrentWeatherUseCase(repository: WeatherRepository): CurrentWeatherUseCase =
        CurrentWeatherUseCase(repository)

    @ScreenScope
    @Provides
    fun provideHourlyWeatherForecastUseCase(repository: WeatherRepository): HourlyWeatherForecastUseCase =
        HourlyWeatherForecastUseCase(repository)

    @ScreenScope
    @Provides
    fun provideDailyWeatherForecastUseCase(repository: WeatherRepository): DailyWeatherForecastUseCase =
        DailyWeatherForecastUseCase(repository)

}