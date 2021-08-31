package com.bosha.data.api

import com.bosha.data.BuildConfig
import com.bosha.data.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherApi {

    @Headers("X-API-KEY:${BuildConfig.THE_WEATHER_APIKEY}")
    @GET("onecall")
    suspend fun currentWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "hourly,daily,minutely,alerts",
    ): WeatherResponse
}