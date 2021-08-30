package com.bosha.domain.entities

data class HourlyForecast(
    val temp: Double,
    val visibility: Int,
    val uvi: Double,
    val pressure: Int,
    val clouds: Int,
    val feelsLike: Double,
    val windGust: Double,
    val dt: Int,
    val pop: Double,
    val windDeg: Int,
    val dewPoint: Double,
    val weather: List<Weather>,
    val humidity: Int,
    val windSpeed: Double
)