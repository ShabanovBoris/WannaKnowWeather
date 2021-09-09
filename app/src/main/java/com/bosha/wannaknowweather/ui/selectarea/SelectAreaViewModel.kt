package com.bosha.wannaknowweather.ui.selectarea

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.common.WeatherGeocoder
import com.bosha.domain.common.collectSuccess
import com.bosha.domain.usecases.CurrentWeatherUseCase
import com.bosha.wannaknowweather.utils.location
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectAreaViewModel(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val weatherGeocoder: WeatherGeocoder,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            this::class.java.simpleName,
            throwable.toString()
        )
    }

    private val _selectedResultUi: MutableStateFlow<List<SearchResultUi>> =
        MutableStateFlow(emptyList())
    val selectedResult get() = _selectedResultUi.asStateFlow()

    fun findCity(cityName: String) {
        viewModelScope.launch(handler) {
            val resultList = mutableListOf<SearchResultUi>()

            weatherGeocoder.getLocationsByName(cityName).forEach {
                val coords = WeatherCoordinates(it.latitude, it.longitude)

                currentWeatherUseCase(coords).collectSuccess { weather ->
                    resultList.add(
                        SearchResultUi(
                            it.getAddressLine(0),
                            coords,
                            weather.temp
                        )
                    )
                }
            }
            _selectedResultUi.value = resultList
        }
    }

    fun selectLocation(coordinates: WeatherCoordinates) {
        sharedPreferences.location = coordinates
    }

    data class SearchResultUi(
        val locationName: String,
        val coordinates: WeatherCoordinates,
        val temp: Double
    )
}