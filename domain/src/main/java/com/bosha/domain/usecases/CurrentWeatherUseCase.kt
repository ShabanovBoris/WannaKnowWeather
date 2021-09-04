package com.bosha.domain.usecases

import com.bosha.domain.common.WeatherCoordinatesLocation
import com.bosha.domain.repositories.WeatherRepository

class CurrentWeatherUseCase (private val repository: WeatherRepository) {

     operator fun invoke(weatherCoordinatesLocation: WeatherCoordinatesLocation) =
        repository.getCurrentWeather(weatherCoordinatesLocation)

}