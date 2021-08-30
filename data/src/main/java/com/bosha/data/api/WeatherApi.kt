package com.bosha.data.api

import com.bosha.data.dto.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

//    "onecall?lat=33.44&lon=-94.04&units=metric&appid=7aafcdf79a81a7f3d4dd1bb314773728&exclude=hourly,daily,minutely,alerts"
    @GET("onecall?lat=33.44&lon=-94.04&units=metric&appid=7aafcdf79a81a7f3d4dd1bb314773728&exclude=hourly,daily,minutely,alerts")
    suspend fun currentWeather(

    ): Response
}