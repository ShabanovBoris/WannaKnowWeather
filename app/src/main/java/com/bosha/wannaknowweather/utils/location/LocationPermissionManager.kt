package com.bosha.wannaknowweather.utils.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
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

    private var snackbarWasShown: Boolean = false

    private fun executeListeners(boolean: Boolean) {
        listeners.forEach {
            it(boolean)
        }
    }

    private fun getActivity() = requireNotNull(activity)

    init {
        if (registry == null)
            registry = getActivity()
                .registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    executeListeners(it)
                    if (it) clear()
                }
    }

    fun addListener(action: (Boolean) -> Unit) {
        listeners.add(action)
    }

    /**
     * if i want to delete some custom listeners
     */
    fun deleteListener(action: (Boolean) -> Unit) {
        listeners.remove(action)
    }

    fun locationPermission(block: ((granted: Boolean) -> Unit)?) {
        registry ?: return
        block?.let {
            addListener(it)
        }

        if (getActivity().hasLocationPermission) {
            executeListeners(true)
            return
        }

        registry?.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun locationPermissionWithSnackBar(showOnce: Boolean = false, onSuccess: Context.() -> Unit) {
        if (getActivity().hasLocationPermission) {
            return
        }
        if (showOnce) {
            if (snackbarWasShown) return
        }
        snackbarWasShown = true

        locationPermission {
            if (it) {
                getActivity().onSuccess()
            } else {
                Snackbar.make(
                    getActivity(),
                    getActivity().findViewById(android.R.id.content),
                    "Allow location permission for define your location",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Grant") {
                        locationPermission { activity?.onSuccess() }
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

