package com.bosha.wannaknowweather.utils.LocationPermisson

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.bosha.domain.common.WeatherCoordinatesLocation
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun Context.getLastLocation(block: ((coordinates: WeatherCoordinatesLocation) -> Unit)? = null): Boolean {

    if (!hasLocationPermission) return false

    LocationServices.getFusedLocationProviderClient(this).lastLocation
        .addOnCompleteListener { taskLocation ->
            if (taskLocation.isSuccessful && taskLocation.result != null) {

                block?.invoke(
                    WeatherCoordinatesLocation(
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