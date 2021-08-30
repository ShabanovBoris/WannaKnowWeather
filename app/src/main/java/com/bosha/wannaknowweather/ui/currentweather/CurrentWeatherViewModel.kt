package com.bosha.wannaknowweather.ui.currentweather

import androidx.lifecycle.ViewModel
import com.bosha.domain.common.WeatherLocation
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.wannaknowweather.utils.logError
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.flow

//54.1836563 37.6426194
class CurrentWeatherViewModel(
    private val currentWeatherUseCase: CurrentWeatherUseCase
    ) : ViewModel() {

    private val handler = CoroutineExceptionHandler(::logError)

//    private val _actionStateFlow: MutableStateFlow<Weather> =
//        MutableStateFlow(ActionState.EMPTY)
//    val actionStateFlow get() = _actionStateFlow.asStateFlow()

    val weatherFlow = flow {
        emit(currentWeatherUseCase(WeatherLocation(54.1836563, 37.6426194)))
    }

}