package com.bosha.data.api

object WeatherConstants {
    fun getImageUrl(code: String) =
        "https://openweathermap.org/img/wn/$code@2x.png"
}