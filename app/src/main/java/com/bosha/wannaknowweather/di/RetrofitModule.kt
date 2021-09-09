package com.bosha.wannaknowweather.di

import com.bosha.wannaknowweather.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().apply {
            baseUrl(BuildConfig.BASE_URL)
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
        }.build()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apikey: Interceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addNetworkInterceptor(apikey)
            .build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("X-API-KEY", com.bosha.data.BuildConfig.THE_WEATHER_APIKEY)
            .build()
        chain.proceed(request)
    }
}