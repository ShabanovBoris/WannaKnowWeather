package com.bosha.wannaknowweather.ui.di

import com.bosha.wannaknowweather.MainActivity
import com.bosha.wannaknowweather.di.DataModule
import com.bosha.wannaknowweather.di.UseCaseModule
import com.bosha.wannaknowweather.di.WeatherApiModule
import com.bosha.wannaknowweather.di.scopes.ScreenScope
import com.bosha.wannaknowweather.ui.currentweather.CurrentWeatherFragment
import dagger.Subcomponent

@ScreenScope
@Subcomponent(modules = [WeatherApiModule::class, UseCaseModule::class, DataModule::class])
interface ScreenComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): ScreenComponent
    }

    fun inject(fragment: CurrentWeatherFragment)
    fun inject(activity: MainActivity)
}