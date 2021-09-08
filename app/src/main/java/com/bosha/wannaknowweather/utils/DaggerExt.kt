package com.bosha.wannaknowweather.utils

import android.content.SharedPreferences
import com.bosha.wannaknowweather.WeatherApp
import com.bosha.wannaknowweather.ui.MainScreenActivity
import com.bosha.wannaknowweather.ui.currentweather.CurrentWeatherFragment
import com.bosha.wannaknowweather.ui.dailyforecast.DailyForecastFragment
import com.bosha.wannaknowweather.ui.di.Screen
import com.bosha.wannaknowweather.ui.selectarea.SelectAreaFragment
import com.bosha.wannaknowweather.utils.location.LocationPermissionManager


internal fun <T> T.injectDeps() {
    when (this) {
        is CurrentWeatherFragment ->
            (requireActivity() as Screen).screenComponent.inject(this@injectDeps)

        is SelectAreaFragment ->
            (requireActivity() as Screen).screenComponent.inject(this@injectDeps)

        is DailyForecastFragment ->
            (requireActivity() as Screen).screenComponent.inject(this@injectDeps)
    }
}

internal fun MainScreenActivity.createScreenComponent(
    prefs: SharedPreferences,
    locManager: LocationPermissionManager
) =
    (application as WeatherApp).appComponent.plusMainScreenComponent().create(prefs, locManager)
        .apply {
            inject(this@createScreenComponent)
        }