package com.bosha.wannaknowweather.ui.currentweather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.common.WeatherGeocoder
import com.bosha.domain.common.takeSuccess
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.entities.HourlyForecast
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.domain.usecases.HourlyWeatherForecastUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val hourlyWeatherForecastUseCase: HourlyWeatherForecastUseCase,
    private val weatherGeocoder: WeatherGeocoder
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            this::class.java.simpleName,
            throwable.toString()
        )
    }

    //TODO create cache in future
    var lastKnown: WeatherCoordinates? = null
        set(value) {
            field = value
            loadData(requireNotNull(value))
        }

    private val _weather: MutableStateFlow<WeatherUi?> =
        MutableStateFlow(null)
    val weather get() = _weather.asStateFlow()

    private val _sideEffect: MutableStateFlow<SideEffects> =
        MutableStateFlow(SideEffects.LOADING)
    val sideEffect get() = _sideEffect.asStateFlow()

    init {
        val location = lastKnown
//        if (location == null) {
//            //TODO the first query in app
//            _sideEffect.value = SideEffects.EMPTY_LOCATION
//        } else {
//            loadData(location)
//        }
        when (location) {
            null -> _sideEffect.value = SideEffects.EMPTY_LOCATION
            else -> loadData(location)
        }
    }

    private fun loadData(coordinates: WeatherCoordinates) = viewModelScope.launch(handler) {
        _sideEffect.value = SideEffects.LOADING
        val areaName = weatherGeocoder.getAreaName(coordinates)

        /**
         * Combine 2 flows(1 day forecast and current weather) in 1 data class
         */
        val hourlyForecast = hourlyWeatherForecastUseCase(coordinates)
            .takeSuccess {
                _sideEffect.value = SideEffects.ERROR
            }

        currentWeatherUseCase(coordinates)
            .takeSuccess {
                _sideEffect.value = SideEffects.ERROR
            }
            .combine(hourlyForecast) { current, forecast ->
                WeatherUi(
                    current.data,
                    forecast.data,
                    areaName[0],
                    areaName[1]
                )
            }
            .collect {
                _weather.value = it
                _sideEffect.value = SideEffects.LOADED
            }
    }


//        viewModelScope.launch {
//            getCachePizzaUseCase().collect {
//                _pizzaResultFlow.value = it
//            }
//        }


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