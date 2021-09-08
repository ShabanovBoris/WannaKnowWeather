package com.bosha.wannaknowweather.ui.di

import android.content.SharedPreferences
import com.bosha.wannaknowweather.di.DataModule
import com.bosha.wannaknowweather.di.UseCaseModule
import com.bosha.wannaknowweather.di.WeatherApiModule
import com.bosha.wannaknowweather.di.WeatherGeocoderModule
import com.bosha.wannaknowweather.di.scopes.ScreenScope
import com.bosha.wannaknowweather.ui.MainScreenActivity
import com.bosha.wannaknowweather.ui.currentweather.CurrentWeatherFragment
import com.bosha.wannaknowweather.ui.dailyforecast.DailyForecastFragment
import com.bosha.wannaknowweather.ui.selectarea.SelectAreaFragment
import com.bosha.wannaknowweather.utils.location.LocationPermissionManager
import dagger.BindsInstance
import dagger.Subcomponent

@ScreenScope
@Subcomponent(
    modules = [WeatherApiModule::class, UseCaseModule::class, DataModule::class,
        WeatherGeocoderModule::class]
)
interface ScreenComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance preferences: SharedPreferences,
            @BindsInstance locationPermissionManager: LocationPermissionManager
        ): ScreenComponent
    }

    fun inject(fragment: CurrentWeatherFragment)
    fun inject(fragment: SelectAreaFragment)
    fun inject(fragment: DailyForecastFragment)
    fun inject(screenActivity: MainScreenActivity)
}