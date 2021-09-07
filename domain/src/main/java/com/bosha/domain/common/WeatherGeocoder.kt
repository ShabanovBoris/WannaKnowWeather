package com.bosha.domain.common

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import java.io.IOException


class WeatherGeocoder(
    private val appContext: Context
) {

    private val geocoder = Geocoder(appContext)

    fun getLocationAddressOrNull(weatherCoordinates: WeatherCoordinates): Address? {
        require(appContext is Application)
        return try {
            geocoder.getFromLocation(
                weatherCoordinates.lat,
                weatherCoordinates.lon,
                1
            ).firstOrNull()
        } catch (e: IOException) {
            Log.e(this::class.java.simpleName, "getLocationName error: ${e.localizedMessage}")
            return null
        }
    }

    fun getAreaName(weatherCoordinates: WeatherCoordinates): List<String> {
        val address = getLocationAddressOrNull(weatherCoordinates)
        return listOf(
            address?.adminArea ?: "",
            address?.locality ?: "",
        )
    }

    fun getLocationsByName(name: String): List<Address> {
        require(appContext is Application)
        return try {
            geocoder.getFromLocationName(
                name,
                5
            )
        } catch (e: IOException) {
            Log.e(this::class.java.simpleName, "getLocationName error: ${e.localizedMessage}")
            return emptyList()
        }
    }
}