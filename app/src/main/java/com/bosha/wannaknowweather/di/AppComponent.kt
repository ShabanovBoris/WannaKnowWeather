package com.bosha.wannaknowweather.di

import dagger.Component

@Component(
    modules = [NetworkModule::class]
)
interface AppComponent {
}