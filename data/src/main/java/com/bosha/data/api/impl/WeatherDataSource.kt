package com.bosha.data.api.impl

import com.bosha.data.api.WeatherApi
import com.bosha.data.mappers.WeatherResponseMapper
import com.bosha.domain.common.WeatherCoordinatesLocation
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.entities.Weather
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherDataSource @Inject constructor(
    private val api: WeatherApi,
    private val mapper: WeatherResponseMapper,//todo
    private val dispatcher: CoroutineDispatcher? = null
) {
    fun getCurrentWeatherByLocation(weatherCoordinatesLocation: WeatherCoordinatesLocation): Flow<CurrentWeather> =
        flow {
            emit(
                api.currentWeatherByLocation(weatherCoordinatesLocation.lat, weatherCoordinatesLocation.lon)
                    .let {
                        CurrentWeather(
                            sunrise = it.current.sunrise,
                            temp = it.current.temp,
                            visibility = it.current.visibility,
                            uvi = it.current.uvi,
                            pressure = it.current.pressure,
                            clouds = it.current.clouds,
                            feelsLike = it.current.feelsLike,
                            windGust = it.current.windGust,
                            dt = it.current.dt,
                            windDeg = it.current.windDeg,
                            dewPoint = it.current.dewPoint,
                            sunset = it.current.sunset,
                            weather = it.current.weather.map {
                                Weather(
                                    it.icon,
                                    it.description,
                                    it.main,
                                    it.id
                                )
                            },
                            humidity = it.current.humidity,
                            windSpeed = it.current.windSpeed
                        )
                    }
            )

        }
            .map { it }//todo
            .flowOn(dispatcher ?: Dispatchers.Main.immediate)
}