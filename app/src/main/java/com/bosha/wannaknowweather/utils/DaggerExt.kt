package com.bosha.wannaknowweather.utils

import com.bosha.wannaknowweather.WeatherApp
import com.bosha.wannaknowweather.ui.MainScreenActivity
import com.bosha.wannaknowweather.ui.currentweather.CurrentWeatherFragment
import com.bosha.wannaknowweather.ui.di.Screen
import com.bosha.wannaknowweather.ui.selectarea.SelectAreaFragment


internal fun <T> T.injectDeps() {
    when (this) {
        is CurrentWeatherFragment ->
            (requireActivity() as Screen).screenComponent.inject(this@injectDeps)

        is SelectAreaFragment ->
            (requireActivity() as Screen).screenComponent.inject(this@injectDeps)
    }
}

internal fun MainScreenActivity.createScreenComponent() =
    (application as WeatherApp).appComponent.plusMainScreenComponent().create()
        .apply {
            inject(this@createScreenComponent)
        }