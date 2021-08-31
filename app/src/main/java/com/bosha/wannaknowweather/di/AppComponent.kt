package com.bosha.wannaknowweather.di

import android.content.Context
import com.bosha.wannaknowweather.ui.di.ScreenComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RetrofitModule::class, Subcomponents::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance appContext: Context): AppComponent
    }

    fun plusMainScreenComponent(): ScreenComponent.Factory

}
