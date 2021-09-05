package com.bosha.wannaknowweather.utils.locationPermisson

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.bosha.domain.common.WeatherCoordinates
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun Context.getLastLocation(block: ((coordinates: WeatherCoordinates) -> Unit)? = null): Boolean {

    if (!hasLocationPermission) return false

    LocationServices.getFusedLocationProviderClient(this).lastLocation
        .addOnCompleteListener { taskLocation ->
            if (taskLocation.isSuccessful && taskLocation.result != null) {

                block?.invoke(
                    WeatherCoordinates(
                        taskLocation.result.latitude,
                        taskLocation.result.longitude
                    )
                )

            } else {
                Log.e("LocationActivity", "getLastLocation:exception", taskLocation.exception)
            }
        }

    return true
}