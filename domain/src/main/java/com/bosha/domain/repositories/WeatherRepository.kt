package com.bosha.domain.repositories

import com.bosha.domain.common.Result
import com.bosha.domain.common.WeatherCoordinatesLocation
import com.bosha.domain.entities.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

     fun getCurrentWeather(weatherCoordinatesLocation: WeatherCoordinatesLocation): Flow<Result<CurrentWeather>>
}