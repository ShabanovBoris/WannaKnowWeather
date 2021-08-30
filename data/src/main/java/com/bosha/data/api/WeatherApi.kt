package com.bosha.data.api

import com.bosha.data.BuildConfig
import com.bosha.data.dto.WeatherResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {

    //    "onecall?lat=33.44&lon=-94.04&units=metric&appid=7aafcdf79a81a7f3d4dd1bb314773728&exclude=hourly,daily,minutely,alerts"
    @GET("onecall")
    suspend fun currentWeatherByLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "hourly,daily,minutely,alerts",
        //TODO delete key
        @Query("appid") key: String = BuildConfig.THE_WEATHER_APIKEY,
    ): WeatherResponse
}