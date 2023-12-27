package com.atafelska.crosswordapplication.network

import io.ktor.client.*

interface HttpClientConfigProvider {
    fun provideConfig(): ((HttpClientConfig<*>) -> Unit)
}
