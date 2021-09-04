package com.bosha.domain.usecases

import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.repositories.WeatherRepository

class HourlyWeatherForecastUseCase  (private val repository: WeatherRepository) {

    operator fun invoke(weatherCoordinates: WeatherCoordinates) =
        repository.getHourlyForecastWeather(weatherCoordinates)

}