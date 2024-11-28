package com.atafelska.crosswordapplication.di

import com.atafelska.crosswordapplication.network.HttpClientConfigProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.Context

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun httpClientConfigProvider():HttpClientConfigProvider = object : HttpClientConfigProvider {
        override fun provideConfig(): (HttpClientConfig<*>) -> Unit = {
            it.install("OtelInterceptor") {
                plugin(HttpSend).intercept { request ->
                    W3CTraceContextPropagator.getInstance().inject(
                        Context.current(),
                        request
                    ) { requestCarrier, key, value ->
                        requestCarrier?.headers?.append(key, value)
                    }
                    execute(request)
                }
            }
        }
    }
}
