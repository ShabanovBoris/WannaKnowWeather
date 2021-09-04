package com.bosha.wannaknowweather.utils

import com.bosha.wannaknowweather.ui.MainScreenActivity
import com.bosha.wannaknowweather.WeatherApp
import com.bosha.wannaknowweather.ui.di.Screen
import com.bosha.wannaknowweather.ui.currentweather.CurrentWeatherFragment

fun CurrentWeatherFragment.injectDeps() =
    (requireActivity() as Screen).screenComponent.inject(this@injectDeps)

fun MainScreenActivity.createScreenComponent() =
    (application as WeatherApp).appComponent.plusMainScreenComponent().create()
        .apply{
            inject(this@createScreenComponent)
        }