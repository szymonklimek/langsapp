package com.atafelska.crosswordapplication.di

import com.atafelska.crossword.android.generated.apis.CategoriesApi
import com.atafelska.crossword.android.generated.apis.ExplanationsApi
import com.atafelska.crosswordapplication.network.Environment
import com.atafelska.crosswordapplication.network.HttpClientConfigProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ContentModule {
    @Provides
    fun categoriesApi(
        environment: Environment,
        httpClientConfigProvider: HttpClientConfigProvider
    ): CategoriesApi =
        CategoriesApi(
            baseUrl = environment.serviceUrl,
            httpClientConfig = httpClientConfigProvider.provideConfig()
        )

    @Provides
    fun explanationsApi(
        environment: Environment,
        httpClientConfigProvider: HttpClientConfigProvider
    ): ExplanationsApi =
        ExplanationsApi(
            baseUrl = environment.serviceUrl,
            httpClientConfig = httpClientConfigProvider.provideConfig()
        )
}
