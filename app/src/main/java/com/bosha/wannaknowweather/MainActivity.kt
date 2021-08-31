package com.bosha.wannaknowweather

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.bosha.wannaknowweather.ui.di.Screen
import com.bosha.wannaknowweather.ui.di.ScreenComponent
import com.bosha.wannaknowweather.utils.createScreenComponent

class MainActivity : AppCompatActivity(R.layout.activity_main), Screen {

    override val screenComponent: ScreenComponent by lazy {
        createScreenComponent()
    }


}
