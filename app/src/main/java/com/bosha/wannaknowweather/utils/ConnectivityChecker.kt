package com.bosha.wannaknowweather.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.bosha.wannaknowweather.R

object ConnectivityChecker {
    fun checkConnectionWithToast(activity: Activity) {
        val cm = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm.activeNetwork == null)
        Toast.makeText(activity, activity.getString(R.string.connect_info), Toast.LENGTH_LONG).show()
    }
}