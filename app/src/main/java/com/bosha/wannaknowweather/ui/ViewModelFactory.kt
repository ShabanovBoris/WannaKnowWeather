package com.bosha.wannaknowweather.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bosha.domain.common.WeatherGeocoder
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.domain.usecases.HourlyWeatherForecastUseCase
import com.bosha.wannaknowweather.di.scopes.ScreenScope
import com.bosha.wannaknowweather.ui.currentweather.CurrentWeatherViewModel
import com.bosha.wannaknowweather.ui.selectarea.SelectAreaViewModel
import javax.inject.Inject

@ScreenScope
class ViewModelFactory @Inject constructor(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val hourlyWeatherForecastUseCase: HourlyWeatherForecastUseCase,
    private val weatherGeocoder: WeatherGeocoder
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {

        CurrentWeatherViewModel::class.java -> CurrentWeatherViewModel(
            currentWeatherUseCase,
            hourlyWeatherForecastUseCase,
            weatherGeocoder
        )

        SelectAreaViewModel::class.java -> SelectAreaViewModel(
            currentWeatherUseCase,
            weatherGeocoder
        )

        else -> error("$modelClass is not registered ViewModel")
    } as T
}
