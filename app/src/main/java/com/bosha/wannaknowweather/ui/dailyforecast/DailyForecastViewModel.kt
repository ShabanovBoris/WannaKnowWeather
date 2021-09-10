package com.bosha.wannaknowweather.ui.dailyforecast

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.common.collectSuccess
import com.bosha.domain.entities.DailyForecast
import com.bosha.domain.usecases.DailyWeatherForecastUseCase
import com.bosha.wannaknowweather.utils.savedLocation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DailyForecastViewModel(
    private val useCase: DailyWeatherForecastUseCase,
    sharedPreferences: SharedPreferences
) : ViewModel() {

    private val handler = CoroutineExceptionHandler { context, throwable ->
        Log.e(
            this::class.java.simpleName,
            throwable.toString() + context
        )
        throwable.printStackTrace()
    }

    private val _forecast: MutableStateFlow<List<DailyForecast>?> =
        MutableStateFlow(null)
    val forecast get() = _forecast.asStateFlow()

    private val savedLocation by sharedPreferences.savedLocation()

    init {
        val loc = savedLocation
        if (loc != null) loadData(loc)
    }

    private fun loadData(coordinates: WeatherCoordinates) = viewModelScope.launch(handler) {
        useCase(coordinates).collectSuccess { list ->
            list.forEach {
                Log.e("TAG", "loadData: $it", )
            }
            _forecast.value = list
        }
    }
}