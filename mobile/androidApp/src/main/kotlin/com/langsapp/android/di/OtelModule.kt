package com.atafelska.crosswordapplication.di

import com.atafelska.crosswordapplication.common.AppInformation
import com.atafelska.crosswordapplication.network.Environment
import com.atafelska.crosswordapplication.otel.Otel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.TracerBuilder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OtelModule {

    @Singleton
    @Provides
    fun openTelemetry(
        environment: Environment,
        appInformation: AppInformation
    ): OpenTelemetry = Otel(environment, appInformation)

    @Provides
    fun tracerBuilder(openTelemetry: OpenTelemetry): TracerBuilder =
        openTelemetry.tracerProvider.tracerBuilder("crossword-android")
}
