package com.bosha.wannaknowweather.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.bosha.wannaknowweather.R
import com.bosha.wannaknowweather.ui.di.Screen
import com.bosha.wannaknowweather.ui.di.ScreenComponent
import com.bosha.wannaknowweather.utils.ConnectivityChecker
import com.bosha.wannaknowweather.utils.createScreenComponent
import com.bosha.wannaknowweather.utils.location.LocationPermissionManager
import com.bosha.wannaknowweather.utils.location.getLastLocation
import javax.inject.Inject

class MainScreenActivity : AppCompatActivity(R.layout.activity_main), Screen{

    @Inject
    lateinit var permissionManager: LocationPermissionManager

    override val screenComponent: ScreenComponent by lazy {
        createScreenComponent(
            getPreferences(Context.MODE_PRIVATE),
            LocationPermissionManager(this)
        )
    }

    override fun onStart() {
        super.onStart()
        ConnectivityChecker.checkConnectionWithToast(this)
    }
}
