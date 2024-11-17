package com.langsapp.devoptions

import com.langsapp.config.AppConfig
import com.langsapp.config.KeyValueStorage
import com.langsapp.devoptions.model.ApiEnvironment

class DevOptionsRepository(
    private val appConfig: AppConfig,
    private val keyValueStorage: KeyValueStorage,
) {
    fun getPossibleEnvironments(): List<ApiEnvironment> = listOf(
        ApiEnvironment("prod", "https://api.langs.app"),
        ApiEnvironment("local_android", "http://10.0.2.2:8080"),
        ApiEnvironment("local_iOS", "http://localhost:8080"),
    )

    fun getSelectedEnvironment(): ApiEnvironment =
        keyValueStorage
            .get(DEV_OPTIONS_KEY_SELECTED_ENV_NAME)
            ?.let {
                ApiEnvironment(
                    name = it,
                    apiUrl = keyValueStorage.get(DEV_OPTIONS_KEY_SELECTED_ENV_URL) ?: "",
                )
            }
            ?: getPossibleEnvironments().first()

    fun saveSelectedEnvironment(apiEnvironment: ApiEnvironment) {
        keyValueStorage.set(DEV_OPTIONS_KEY_SELECTED_ENV_NAME, apiEnvironment.name)
        keyValueStorage.set(DEV_OPTIONS_KEY_SELECTED_ENV_URL, apiEnvironment.apiUrl)
        appConfig.apiEnvironment = apiEnvironment
    }

    companion object {
        private const val DEV_OPTIONS_KEY_SELECTED_ENV_NAME = "DEV_OPTIONS_KEY_SELECTED_ENV_NAME"
        private const val DEV_OPTIONS_KEY_SELECTED_ENV_URL = "DEV_OPTIONS_KEY_SELECTED_ENV_URL"
    }
}
