package com.bosha.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherResponse(
	@field:SerializedName("current")
	val current: Current,
	@field:SerializedName("daily")
	val daily: List<DailyItem>,
	@field:SerializedName("lon")
	val lon: Double,
	@field:SerializedName("hourly")
	val hourly: List<HourlyItem>,
	@field:SerializedName("lat")
	val lat: Double
) : Parcelable

@Parcelize
data class FeelsLike(
	@field:SerializedName("eve")
	val eve: Double,
	@field:SerializedName("night")
	val night: Double,
	@field:SerializedName("day")
	val day: Double,
	@field:SerializedName("morn")
	val morn: Double
) : Parcelable

@Parcelize
data class DailyItem(
	@field:SerializedName("moonset")
	val moonset: Int,
	@field:SerializedName("sunrise")
	val sunrise: Int,
	@field:SerializedName("temp")
	val temp: Temp,
	@field:SerializedName("moon_phase")
	val moonPhase: Double,
	@field:SerializedName("uvi")
	val uvi: Double,
	@field:SerializedName("moonrise")
	val moonrise: Int,
	@field:SerializedName("pressure")
	val pressure: Int,
	@field:SerializedName("clouds")
	val clouds: Int,
	@field:SerializedName("feels_like")
	val feelsLike: FeelsLike,
	@field:SerializedName("wind_gust")
	val windGust: Double?,
	@field:SerializedName("dt")
	val dt: Int,
	@field:SerializedName("pop")
	val pop: Double,
	@field:SerializedName("wind_deg")
	val windDeg: Int,
	@field:SerializedName("dew_point")
	val dewPoint: Double,
	@field:SerializedName("sunset")
	val sunset: Int,
	@field:SerializedName("weather")
	val weather: List<WeatherItem>,
	@field:SerializedName("humidity")
	val humidity: Int,
	@field:SerializedName("wind_speed")
	val windSpeed: Double,
	@field:SerializedName("rain")
	val rain: Double?
) : Parcelable

@Parcelize
data class Temp(
	@field:SerializedName("min")
	val min: Double,
	@field:SerializedName("max")
	val max: Double,
	@field:SerializedName("eve")
	val eve: Double,
	@field:SerializedName("night")
	val night: Double,
	@field:SerializedName("day")
	val day: Double,
	@field:SerializedName("morn")
	val morn: Double
) : Parcelable

@Parcelize
data class Current(
	@field:SerializedName("sunrise")
	val sunrise: Int,
	@field:SerializedName("temp")
	val temp: Double,
	@field:SerializedName("visibility")
	val visibility: Int,
	@field:SerializedName("uvi")
	val uvi: Double,
	@field:SerializedName("pressure")
	val pressure: Int,
	@field:SerializedName("clouds")
	val clouds: Int,
	@field:SerializedName("feels_like")
	val feelsLike: Double,
	@field:SerializedName("wind_gust")
	val windGust: Double,
	@field:SerializedName("dt")
	val dt: Int,
	@field:SerializedName("wind_deg")
	val windDeg: Int,
	@field:SerializedName("dew_point")
	val dewPoint: Double,
	@field:SerializedName("sunset")
	val sunset: Int,
	@field:SerializedName("weather")
	val weather: List<WeatherItem>,
	@field:SerializedName("humidity")
	val humidity: Int,
	@field:SerializedName("wind_speed")
	val windSpeed: Double
) : Parcelable


@Parcelize
data class WeatherItem(
	@field:SerializedName("icon")
	val icon: String,
	@field:SerializedName("description")
	val description: String,
	@field:SerializedName("main")
	val main: String,
	@field:SerializedName("id")
	val id: Int
) : Parcelable

@Parcelize
data class HourlyItem(
	@field:SerializedName("temp")
	val temp: Double,
	@field:SerializedName("visibility")
	val visibility: Int,
	@field:SerializedName("uvi")
	val uvi: Double,
	@field:SerializedName("pressure")
	val pressure: Int,
	@field:SerializedName("clouds")
	val clouds: Int,
	@field:SerializedName("feels_like")
	val feelsLike: Double,
	@field:SerializedName("wind_gust")
	val windGust: Double,
	@field:SerializedName("dt")
	val dt: Int,
	@field:SerializedName("pop")
	val pop: Double,
	@field:SerializedName("wind_deg")
	val windDeg: Int,
	@field:SerializedName("dew_point")
	val dewPoint: Double,
	@field:SerializedName("weather")
	val weather: List<WeatherItem>,
	@field:SerializedName("humidity")
	val humidity: Int,
	@field:SerializedName("wind_speed")
	val windSpeed: Double
) : Parcelable
