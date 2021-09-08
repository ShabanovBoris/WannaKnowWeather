package com.bosha.domain.entities

data class DailyForecast(
    val moonset: Int,
    val sunrise: Int,
    val temp: Temp,
    val moonPhase: Double,
    val uvi: Double,
    val moonrise: Int,
    val pressure: Int,
    val clouds: Int,
    val windGust: Double,
    val dt: Int,
    val pop: Double,
    val windDeg: Int,
    val dewPoint: Double,
    val sunset: Int,
    val weather: List<Weather>,
    val humidity: Int,
    val windSpeed: Double,
    val rain: Double
)