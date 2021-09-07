package com.bosha.wannaknowweather.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.bosha.domain.common.WeatherCoordinates
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val KEY_LOCATION = "key_location"

internal var SharedPreferences.location: WeatherCoordinates?
    set(value) {
        value ?: return
        edit {
            putString(KEY_LOCATION, "${value.lat}:${value.lon}")
        }
    }
    get() {
        if (contains(KEY_LOCATION).not()) return null
        edit {
            (getString(KEY_LOCATION, null) ?: "")
                .also {
                    val list = it.split(":")
                    return WeatherCoordinates(list[0].toDouble(), list[1].toDouble())
                }
        }
        return null
    }


internal fun SharedPreferences.listenLocation(): Flow<WeatherCoordinates> {
    return callbackFlow {

        val listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == KEY_LOCATION) {
                    if (sharedPreferences.location != null)
                        trySend(requireNotNull(sharedPreferences.location))
                }
            }

        registerOnSharedPreferenceChangeListener(listener)

        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }
}