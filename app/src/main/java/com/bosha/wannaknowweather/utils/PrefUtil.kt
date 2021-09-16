package com.bosha.wannaknowweather.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.bosha.domain.common.WeatherCoordinates
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Simple delegate that uses for getting last used location
 * i.e. user did not grant location permission and we don not want
 * to select location every launch app
 */

private const val KEY_LOCATION = "key_location"

fun SharedPreferences.savedLocation() = SavedLocation(this)

class SavedLocation(private val sharedPreferences: SharedPreferences) :
    ReadWriteProperty<Any?, WeatherCoordinates?> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): WeatherCoordinates? {
        if (sharedPreferences.contains(KEY_LOCATION).not()) return null
        (sharedPreferences.getString(KEY_LOCATION, null) ?: "")
            .also {
                val list = it.split(":")
                return WeatherCoordinates(list[0].toDouble(), list[1].toDouble())
            }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: WeatherCoordinates?) {
        value ?: return
        sharedPreferences.edit {
            putString(KEY_LOCATION, "${value.lat}:${value.lon}")
        }
    }
}


internal fun SharedPreferences.listenLocation(): Flow<WeatherCoordinates> {
    val location by savedLocation()

    return callbackFlow {

        val listener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == KEY_LOCATION) {
                    if (location != null)
                        trySend(requireNotNull(location))
                }
            }

        registerOnSharedPreferenceChangeListener(listener)

        /**
         * unregister when parent coroutine will finish
         */
        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }
}