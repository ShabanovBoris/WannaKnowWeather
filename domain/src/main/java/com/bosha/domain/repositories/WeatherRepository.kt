package com.bosha.domain.repositories

import com.bosha.domain.common.Result
import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.entities.DailyForecast
import com.bosha.domain.entities.HourlyForecast
import kotlinx.coroutines.flow.Flow

typealias HourlyForecastResult = Result<List<HourlyForecast>>
typealias DailyForecastResult = Result<List<DailyForecast>>

interface WeatherRepository {

    fun getCurrentWeather(weatherCoordinates: WeatherCoordinates): Flow<Result<CurrentWeather>>

    fun getHourlyForecastWeather(weatherCoordinates: WeatherCoordinates): Flow<HourlyForecastResult>

    fun getDailyForecastWeather(weatherCoordinates: WeatherCoordinates): Flow<DailyForecastResult>
}