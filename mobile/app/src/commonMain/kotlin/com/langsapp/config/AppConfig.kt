package com.langsapp.config

import com.langsapp.BuildConfig
import com.langsapp.devoptions.model.ApiEnvironment
import com.langsapp.identity.auth.AuthConfig
import com.langsapp.platform.randomUUID

object AppConfig {
    lateinit var log: Log
    lateinit var keyValueStorage: KeyValueStorage
    lateinit var uniqueInstallationId: String
    lateinit var apiEnvironment: ApiEnvironment
    var devOptionsEnabled: Boolean = false

    var authConfig: AuthConfig = AuthConfig(
        authorizationEndpoint = BuildConfig.APPAUTH_AUTHORIZATION_ENDPOINT,
        tokenEndpoint = BuildConfig.APPAUTH_TOKEN_ENDPOINT,
        registrationEndpoint = BuildConfig.APPAUTH_REGISTRATION_ENDPOINT,
        endSessionEndpoint = BuildConfig.APPAUTH_ENDSESSION_ENDPOINT,
        clientId = BuildConfig.APPAUTH_CLIENT_ID,
        redirectUri = BuildConfig.APPAUTH_REDIRECT_URI,
    )

    fun init(
        log: Log,
        keyValueStorage: KeyValueStorage,
        devOptionsEnabled: Boolean,
    ) {
        this.log = log
        this.keyValueStorage = keyValueStorage
        this.uniqueInstallationId = "installation_id"
            .let { keyValueStorage.get(it) ?: randomUUID().apply { keyValueStorage.set(it, this) } }
        this.devOptionsEnabled = devOptionsEnabled
        this.apiEnvironment = ApiEnvironment(name = "prod", apiUrl = "https://api.langs.app")
    }

    interface Log {
        fun v(message: String)
        fun d(message: String)
        fun i(message: String)
        fun w(message: String)
        fun e(message: String)
        fun wtf(message: String)
    }
}
