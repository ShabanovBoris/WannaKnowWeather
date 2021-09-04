package com.bosha.wannaknowweather.ui.currentweather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosha.domain.common.ErrorResult
import com.bosha.domain.common.PendingResult
import com.bosha.domain.common.SuccessResult
import com.bosha.domain.common.WeatherCoordinatesLocation
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.usecases.CurrentWeatherUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CurrentWeatherViewModel(
    private val currentWeatherUseCase: CurrentWeatherUseCase
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            this::class.java.simpleName,
            throwable.toString()
        )
    }

    //TODO create cache in future
    var lastKnownLocation: WeatherCoordinatesLocation? = null
        set(value) {
            field = value
            load(requireNotNull(value))
        }

    private val _weatherResultFlow: MutableStateFlow<CurrentWeather?> =
        MutableStateFlow(null)
    val weatherResultFlow get() = _weatherResultFlow.asStateFlow()

    private val _sideEffect: MutableStateFlow<SideEffectActions> =
        MutableStateFlow(SideEffectActions.LOADING)
    val sideEffect get() = _sideEffect.asStateFlow()


    init {
        val location = lastKnownLocation
        if (location == null){
            //TODO the first query in app
            load(WeatherCoordinatesLocation(35.3287425, -122.0295577))
        } else {
            load(location)
        }
    }

    private fun load(coordinates: WeatherCoordinatesLocation) {
        viewModelScope.launch(handler) {
            currentWeatherUseCase(coordinates).collect {
                when (it) {
                    is ErrorResult -> {
                        _sideEffect.value = SideEffectActions.ERROR
                    }
                    is PendingResult -> {
                        _sideEffect.value = SideEffectActions.LOADING
                    }
                    is SuccessResult -> {
                        _weatherResultFlow.value = it.data
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

}