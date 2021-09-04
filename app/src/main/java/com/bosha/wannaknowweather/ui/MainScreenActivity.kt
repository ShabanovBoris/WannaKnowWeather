package com.bosha.wannaknowweather.ui

import androidx.appcompat.app.AppCompatActivity
import com.bosha.wannaknowweather.R
import com.bosha.wannaknowweather.ui.di.Screen
import com.bosha.wannaknowweather.ui.di.ScreenComponent
import com.bosha.wannaknowweather.utils.ConnectivityChecker
import com.bosha.wannaknowweather.utils.createScreenComponent

class MainScreenActivity : AppCompatActivity(R.layout.activity_main), Screen {

    override val screenComponent: ScreenComponent by lazy {
        createScreenComponent()
    }

    override fun onStart() {
        super.onStart()
        ConnectivityChecker.checkConnectionWithToast(this)
    }
}
