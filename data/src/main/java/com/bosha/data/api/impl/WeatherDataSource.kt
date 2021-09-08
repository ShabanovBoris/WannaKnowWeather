package com.bosha.data.api.impl

import android.util.Log
import com.bosha.data.api.WeatherApi
import com.bosha.data.mappers.WeatherResponseMapper
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.entities.DailyForecast
import com.bosha.domain.entities.HourlyForecast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherDataSource @Inject constructor(
    private val api: WeatherApi,
    private val mapper: WeatherResponseMapper,
    private val dispatcher: CoroutineDispatcher? = null
) {
    fun getCurrentWeatherByLocation(weatherCoordinates: WeatherCoordinates): Flow<CurrentWeather> =
        flow {
            emit(api.currentWeatherByLocation(weatherCoordinates.lat, weatherCoordinates.lon))
        }
            .map { mapper { it.mapToCurrentWeather() } }
            .flowOn(dispatcher ?: Dispatchers.Main.immediate)


    fun getHourlyWeatherByLocation(weatherCoordinates: WeatherCoordinates): Flow<List<HourlyForecast>> =
        flow {
            emit(api.hourlyWeatherByLocation(weatherCoordinates.lat, weatherCoordinates.lon))
        }
            .map { mapper { it.mapToHourlyForecast() } }
            .flowOn(dispatcher ?: Dispatchers.Main.immediate)

    fun getDailyWeatherByLocation(weatherCoordinates: WeatherCoordinates): Flow<List<DailyForecast>> =
        flow {
            emit(api.dailyWeatherByLocation(weatherCoordinates.lat, weatherCoordinates.lon))
        }
            .map { mapper { it.mapToDailyForecast() } }
            .flowOn(dispatcher ?: Dispatchers.Main.immediate)
}