package com.bosha.wannaknowweather.utils.location

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

    private val listeners = mutableSetOf<(Boolean) -> Unit>()

    private fun executeListeners(boolean: Boolean) {
        listeners.forEach {
            it(boolean)
        }
    }

    init {
        if (registry == null)
            registry = requireNotNull(activity)
                .registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    executeListeners(it)
                }
    }

    fun addOnPermissionListener(action: (Boolean) -> Unit) {
        listeners.add(action)
    }

    fun deleteOnPermissionListener(action: (Boolean) -> Unit) {
        listeners.remove(action)
    }

    fun locationPermission(block: ((granted: Boolean) -> Unit)?) {
        block?.let {
            val listenerWrapper:(granted: Boolean) -> Unit = {
                block(it)
                deleteOnPermissionListener(block)
            }
            addOnPermissionListener(listenerWrapper)
        }

        if (requireNotNull(activity).hasLocationPermission) {
            executeListeners(true)
            return
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
        listeners.clear()
    }
}

