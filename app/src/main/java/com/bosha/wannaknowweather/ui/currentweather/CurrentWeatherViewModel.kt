package com.bosha.wannaknowweather.ui.currentweather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosha.domain.common.ErrorResult
import com.bosha.domain.common.SuccessResult
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.common.WeatherGeocoder
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.domain.usecases.HourlyWeatherForecastUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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
            load(requireNotNull(value))
        }

    private val _weatherResultFlow: MutableStateFlow<CurrentWeatherUi?> =
        MutableStateFlow(null)
    val weatherResultFlow get() = _weatherResultFlow.asStateFlow()

    private val _sideEffect: MutableStateFlow<SideEffectActions> =
        MutableStateFlow(SideEffectActions.LOADING)
    val sideEffect get() = _sideEffect.asStateFlow()

    init {
        val location = lastKnown
        if (location == null) {
            //TODO the first query in app
            load(WeatherCoordinates(35.3287425, -122.0295577))
        } else {
            load(location)
        }

        Log.e("TAG", "locations "+weatherGeocoder.getLocationsByName("Тула").toString(), )
        viewModelScope.launch {
            hourlyWeatherForecastUseCase(WeatherCoordinates(54.204836,37.6184915)).collect {
                when (it) {
                    is ErrorResult -> { }
                    is SuccessResult -> {
                        it.data.forEach { weather ->
                            Log.e("TAG", weather.toString(), )
                        }

                    }
                }
            }
        }
    }

    private fun load(coordinates: WeatherCoordinates) {
        viewModelScope.launch(handler) {
            val name = weatherGeocoder.getAreaName(coordinates)

            currentWeatherUseCase(coordinates).collect {
                when (it) {
                    is ErrorResult -> {
                        _sideEffect.value = SideEffectActions.ERROR
                    }
                    is SuccessResult -> {
                        _weatherResultFlow.value = CurrentWeatherUi(
                            it.data,
                            name[0],
                            name[1]
                        )
                        _sideEffect.value = SideEffectActions.LOADED
//                        putCachePizzaUseCase(it.data)
                    }
                }
            }
        }
//        viewModelScope.launch {
//            getCachePizzaUseCase().collect {
//                _pizzaResultFlow.value = it
//            }
//        }
    }

    enum class SideEffectActions {
        LOADING,
        ERROR,
        LOADED
    }

    data class CurrentWeatherUi(
        val weather: CurrentWeather,
        val areaName: String,
        val locality: String
    )
}