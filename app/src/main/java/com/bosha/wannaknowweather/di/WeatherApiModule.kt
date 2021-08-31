package com.bosha.wannaknowweather.di

import com.bosha.data.api.WeatherApi
import com.bosha.wannaknowweather.di.scopes.ScreenScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class WeatherApiModule {

    @ScreenScope
    @Provides
    fun provideHabitApi(retrofit: Retrofit): WeatherApi =
        retrofit.create(WeatherApi::class.java)
}