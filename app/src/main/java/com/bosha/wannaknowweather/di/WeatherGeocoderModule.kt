package com.bosha.wannaknowweather.di

import android.content.Context
import com.bosha.domain.common.WeatherGeocoder
import com.bosha.wannaknowweather.di.scopes.ScreenScope
import dagger.Module
import dagger.Provides


@Module
class WeatherGeocoderModule {

    @ScreenScope
    @Provides
    fun provideWeatherGeocoder(appContext: Context): WeatherGeocoder =
        WeatherGeocoder(appContext)
}