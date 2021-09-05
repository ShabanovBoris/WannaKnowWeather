package com.bosha.wannaknowweather.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun Int.unixSecondsToTime(): String {
    val date = Instant.fromEpochSeconds(this.toLong()).toLocalDateTime(TimeZone.UTC)
    return "${date.hour}:${date.minute}"
}
