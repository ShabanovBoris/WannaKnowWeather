package com.bosha.wannaknowweather.utils

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.TextStyle
import java.util.*


internal fun Int.unixSecondsToTime(): String {
    val date = Instant.fromEpochSeconds(this.toLong()).toLocalDateTime(TimeZone.UTC)
    return if (date.minute == 0) "${date.hour}:00"
    else "${date.hour}:${date.minute}"
}


internal fun Int.unixSecondsToDayOfWeek(): String {
    val date = Instant.fromEpochSeconds(this.toLong()).toLocalDateTime(TimeZone.UTC)
    return date.dayOfWeek.name
}

internal fun Int.unixSecondsToDate(): String {
    val date = Instant.fromEpochSeconds(this.toLong()).toLocalDateTime(TimeZone.UTC)
    return "${date.month.ordinal}/${date.dayOfMonth}"
}