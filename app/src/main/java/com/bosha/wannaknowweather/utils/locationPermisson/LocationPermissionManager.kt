package com.bosha.wannaknowweather.utils.locationPermisson

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar

val Context.hasLocationPermission: Boolean
    get() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

/**
 * Need to call [clear] after use for avoid memory leaks
 */
class LocationPermissionManager(
    private var activity: ComponentActivity?
) {

    private var registry: ActivityResultLauncher<String>? = null

    fun locationPermission(block: (granted: Boolean) -> Unit) {
        if (requireNotNull(activity).hasLocationPermission) {
            block(true)
            return
        }

        if (registry == null)
            registry = requireNotNull(activity)
                .registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    block(it)
                }

        registry?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun locationPermissionWithSnackBar(anchorView: View, onSuccess: Context.() -> Unit) {
        locationPermission {
            if (it) {
                activity?.onSuccess()
            } else {
                Snackbar.make(
                    requireNotNull(activity),
                    anchorView,
                    "Permission needs for define your location",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Grant") {
                        locationPermissionWithSnackBar(anchorView, onSuccess)
                    }.show()
            }
        }
    }

    fun clear() {
        activity = null
        registry = null
    }
}

