package com.bosha.wannaknowweather.ui.currentweather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.common.WeatherGeocoder
import com.bosha.domain.common.takeIfSuccess
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

    private val _sideEffect: MutableStateFlow<SideEffectActions> =
        MutableStateFlow(SideEffectActions.LOADING)
    val sideEffect get() = _sideEffect.asStateFlow()

    init {
        _sideEffect.value = SideEffectActions.LOADING
        val location = lastKnown
        if (location == null) {
            //TODO the first query in app
            _sideEffect.value = SideEffectActions.EMPTY_LOCATION
        } else {
            loadData(location)
        }

//        Log.e("TAG", "locations " + weatherGeocoder.getLocationsByName("Тула").toString())
    }

    private fun loadData(coordinates: WeatherCoordinates) = viewModelScope.launch(handler) {
        _sideEffect.value = SideEffectActions.LOADING
        val name = weatherGeocoder.getAreaName(coordinates)

        val hourlyFlow = hourlyWeatherForecastUseCase(coordinates)
            .takeIfSuccess {
                _sideEffect.value = SideEffectActions.ERROR
            }

        currentWeatherUseCase(coordinates)
            .takeIfSuccess {
                _sideEffect.value = SideEffectActions.ERROR
            }
            .combine(hourlyFlow) { current, forecast ->
                WeatherUi(
                    current.data,
                    forecast.data,
                    name[0],
                    name[1]
                )
            }
            .collect {
                _weather.value = it
                _sideEffect.value = SideEffectActions.LOADED
            }
    }


//        viewModelScope.launch {
//            getCachePizzaUseCase().collect {
//                _pizzaResultFlow.value = it
//            }
//        }


    enum class SideEffectActions {
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