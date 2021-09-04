package com.bosha.data.mappers

import com.bosha.data.dto.WeatherResponse
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.entities.HourlyForecast
import com.bosha.domain.entities.Weather
import javax.inject.Inject

class WeatherResponseMapper @Inject constructor() {

    fun WeatherResponse.mapToCurrentWeather(): CurrentWeather =
        CurrentWeather(
            sunrise = current.sunrise,
            temp = current.temp,
            visibility = current.visibility,
            uvi = current.uvi,
            pressure = current.pressure,
            clouds = current.clouds,
            feelsLike = current.feelsLike,
            windGust = current.windGust,
            dt = current.dt,
            windDeg = current.windDeg,
            dewPoint = current.dewPoint,
            sunset = current.sunset,
            weather = current.weather.map {
                Weather(
                    it.icon,
                    it.description,
                    it.main,
                    it.id
                )
            },
            humidity = current.humidity,
            windSpeed = current.windSpeed
        )

    fun WeatherResponse.mapToHourlyForecast(): List<HourlyForecast> =
        hourly.map { hourlyItem ->
            HourlyForecast(
                temp = hourlyItem.temp,
                visibility = hourlyItem.visibility,
                uvi = hourlyItem.uvi,
                pressure = hourlyItem.pressure,
                clouds = hourlyItem.clouds,
                feelsLike = hourlyItem.feelsLike,
                windGust = hourlyItem.windGust,
                dt = hourlyItem.dt,
                windDeg = hourlyItem.windDeg,
                dewPoint = hourlyItem.dewPoint,
                weather = hourlyItem.weather.map {
                    Weather(
                        it.icon,
                        it.description,
                        it.main,
                        it.id
                    )
                },
                humidity = hourlyItem.humidity,
                windSpeed = hourlyItem.windSpeed,
                pop = hourlyItem.pop
            )
        }

    operator fun <T> invoke(scope: WeatherResponseMapper.() -> T): T {
        return scope()
    }
}
