package com.bosha.domain.usecases

import com.bosha.domain.common.WeatherCoordinates
import com.bosha.domain.repositories.WeatherRepository

class DailyWeatherForecastUseCase (private val repository: WeatherRepository) {

    operator fun invoke(weatherCoordinates: WeatherCoordinates) =
        repository.getDailyForecastWeather(weatherCoordinates)

}