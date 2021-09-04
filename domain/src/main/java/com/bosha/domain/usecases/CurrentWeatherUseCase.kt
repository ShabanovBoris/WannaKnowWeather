package com.bosha.domain.usecases

import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.repositories.WeatherRepository

class CurrentWeatherUseCase (private val repository: WeatherRepository) {

     operator fun invoke(weatherCoordinates: WeatherCoordinates) =
        repository.getCurrentWeather(weatherCoordinates)

}