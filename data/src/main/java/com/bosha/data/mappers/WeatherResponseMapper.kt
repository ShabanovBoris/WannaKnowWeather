package com.bosha.data.mappers

import com.bosha.data.dto.WeatherResponse
import com.bosha.domain.entities.*
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

    fun WeatherResponse.mapToDailyForecast(): List<DailyForecast> =
        daily.map { daily ->
            DailyForecast(
                temp = Temp(
                    min = daily.temp.min,
                    max = daily.temp.max,
                    eve = daily.temp.eve,
                    night = daily.temp.night,
                    day = daily.temp.day,
                    morn = daily.temp.morn
                ),
                uvi = daily.uvi,
                pressure = daily.pressure,
                clouds = daily.clouds,
                windGust = daily.windGust ?: 0.0,
                dt = daily.dt,
                windDeg = daily.windDeg,
                dewPoint = daily.dewPoint,
                weather = daily.weather.map {
                    Weather(
                        it.icon,
                        it.description,
                        it.main,
                        it.id
                    )
                },
                humidity = daily.humidity,
                windSpeed = daily.windSpeed,
                pop = daily.pop,
                sunset = daily.sunset,
                moonset = daily.moonset,
                sunrise = daily.sunrise,
                moonPhase = daily.moonPhase,
                moonrise = daily.moonrise,
                rain = daily.rain ?: 0.0
            )
        }

    operator fun <T> invoke(scope: WeatherResponseMapper.() -> T): T {
        return scope()
    }
}
