package com.bosha.wannaknowweather.ui.currentweather

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.common.WeatherGeocoder
import com.bosha.domain.common.getSuccess
import com.bosha.domain.common.takeSuccessOrElse
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.entities.HourlyForecast
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.domain.usecases.HourlyWeatherForecastUseCase
import com.bosha.wannaknowweather.utils.listenLocation
import com.bosha.wannaknowweather.utils.location
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val hourlyWeatherForecastUseCase: HourlyWeatherForecastUseCase,
    private val weatherGeocoder: WeatherGeocoder,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            this::class.java.simpleName,
            throwable.toString()
        )
    }

    var lastKnownLocation: WeatherCoordinates?
        set(value) {
            sharedPreferences.location = value
            loadData(requireNotNull(value))
        }
        get() = sharedPreferences.location

    private val _weather: MutableStateFlow<WeatherUi?> =
        MutableStateFlow(null)
    val weather get() = _weather.asStateFlow()

    private val _sideEffect: MutableStateFlow<SideEffects> =
        MutableStateFlow(SideEffects.LOADING)
    val sideEffect get() = _sideEffect.asStateFlow()

    init {
        val location = lastKnownLocation
        when (location) {
            null -> _sideEffect.value = SideEffects.EMPTY_LOCATION
            else -> loadData(location)
        }
        viewModelScope.launch {
            sharedPreferences.listenLocation()
                .collect {
                    loadData(it) }
        }
    }

    private fun loadData(coordinates: WeatherCoordinates) = viewModelScope.launch(handler) {
        _sideEffect.value = SideEffects.LOADING
        val areaName = weatherGeocoder.getAreaName(coordinates)

        /**
         * Combine 2 flows(1 day forecast and current weather) in 1 data class
         */
        val hourlyForecast = hourlyWeatherForecastUseCase(coordinates)
            .takeSuccessOrElse {
                _sideEffect.value = SideEffects.ERROR
            }

        currentWeatherUseCase(coordinates)
            .takeSuccessOrElse {
                _sideEffect.value = SideEffects.ERROR
            }
            .combine(hourlyForecast) { current, forecast ->
                WeatherUi(
                    current.data,
                    forecast.data.drop(3).take(24),
                    areaName[0],
                    areaName[1]
                )
            }
            .collect {
                _weather.value = it
                _sideEffect.value = SideEffects.LOADED
            }
    }

    enum class SideEffects {
        LOADING,
        ERROR,
        LOADED,
        EMPTY_LOCATION
    }

    data class WeatherUi(
        val currentWeather: CurrentWeather,
        val forecast: List<HourlyForecast>,
        val areaName: String,
        val locality: String
    )
}