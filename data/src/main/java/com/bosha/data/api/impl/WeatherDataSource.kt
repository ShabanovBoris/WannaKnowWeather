package com.bosha.data.api.impl

import com.bosha.data.api.WeatherApi
import com.bosha.data.mappers.WeatherResponseMapper
import com.bosha.domain.common.WeatherLocation
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.entities.Weather

class WeatherDataSource(
    private val api: WeatherApi,
    private val mapper: WeatherResponseMapper//todo
) {
    suspend fun getCurrentWeatherByLocation(weatherLocation: WeatherLocation): CurrentWeather =
        api.currentWeatherByLocation(weatherLocation.lat, weatherLocation.lon)
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
                    weather = it.current.weather.map { Weather(it.icon, it.description, it.main, it.id) },
                    humidity = it.current.humidity,
                    windSpeed = it.current.windSpeed
                )
            }
}