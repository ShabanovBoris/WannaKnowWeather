package com.bosha.domain.repositories

import com.bosha.domain.common.Result
import com.bosha.domain.common.WeatherLocation
import com.bosha.domain.entities.CurrentWeather
import com.bosha.domain.entities.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

     fun getCurrentWeather(weatherLocation: WeatherLocation): Flow<Result<CurrentWeather>>
}