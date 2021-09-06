package com.bosha.wannaknowweather.ui.selectarea

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.common.WeatherGeocoder
import com.bosha.domain.usecases.CurrentWeatherUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectAreaViewModel(
    private val currentWeatherUseCase: CurrentWeatherUseCase,
    private val weatherGeocoder: WeatherGeocoder
) : ViewModel() {
    private val handler = CoroutineExceptionHandler { _, throwable ->
        Log.e(
            this::class.java.simpleName,
            throwable.toString()
        )
    }

    private val _searchResult: MutableStateFlow<List<SearchResult>> =
        MutableStateFlow(emptyList())
    val searchResult get() = _searchResult.asStateFlow()

    //TODO on worker thread
    fun findCity(cityName: String) {
        viewModelScope.launch {
            _searchResult.value = weatherGeocoder.getLocationsByName(cityName).map {
                Log.e("TAG", "findCity: $it", )
                SearchResult(
                    it.getAddressLine(0),
                    WeatherCoordinates(
                        it.latitude,
                        it.longitude
                    ),
                    12.0 // TODO STUB
                )
            }
        }
    }

    data class SearchResult(
        val locationName: String,
        val coordinates: WeatherCoordinates,
        val temp: Double
    )
}