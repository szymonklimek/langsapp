package com.atafelska.crosswordapplication.di

import android.provider.Settings
import com.atafelska.crosswordapplication.BuildConfig
import com.atafelska.crosswordapplication.common.AppInformation
import com.atafelska.crosswordapplication.network.Environment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    fun bindAppInformation(): AppInformation = AppInformation(
        appId = Settings.Secure.ANDROID_ID,
        appVersion = "${BuildConfig.APPLICATION_ID}-${BuildConfig.BUILD_TYPE}-${BuildConfig.VERSION_NAME}",
        appTechnicalName = "android-app-crossword"
    )

    @Provides
    fun environment(): Environment = Environment(
        serviceUrl = BuildConfig.serviceUrl,
        otelCollectorUrl = BuildConfig.otelCollectorUrl
    )
}
