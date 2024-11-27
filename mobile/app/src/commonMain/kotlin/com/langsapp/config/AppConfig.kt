package com.langsapp.config

import com.langsapp.BuildConfig
import com.langsapp.devoptions.model.ApiEnvironment
import com.langsapp.identity.auth.AuthConfig
import com.langsapp.observability.Observability
import com.langsapp.platform.AppInstallationInfo
import com.langsapp.platform.randomUUID
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.api.SendingRequest
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

object AppConfig {
    lateinit var log: Log
    lateinit var keyValueStorage: KeyValueStorage
    lateinit var uniqueInstallationId: String
    lateinit var apiEnvironment: ApiEnvironment
    var devOptionsEnabled: Boolean = false
    lateinit var httpClient: HttpClient
        private set
    lateinit var appInstallationInfo: AppInstallationInfo
        private set
    lateinit var observability: Observability
        private set

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
        observability: Observability,
    ) {
        this.log = log
        this.keyValueStorage = keyValueStorage
        this.uniqueInstallationId = "installation_id"
            .let { keyValueStorage.get(it) ?: randomUUID().apply { keyValueStorage.set(it, this) } }
        this.devOptionsEnabled = devOptionsEnabled
        this.apiEnvironment = ApiEnvironment(name = "prod", apiUrl = "http://10.0.2.2:8080")
        this.observability = observability
        this.appInstallationInfo = AppInstallationInfo(
            installationId = uniqueInstallationId,
            appId = "",
            appVersion = "",
            deviceModel = "",
            deviceManufacturer = "",
            deviceSystemName = "",
            deviceSystemVersion = "",
        )
        this.httpClient = HttpClient {
            val responseTimePlugin = createClientPlugin("ResponseTimePlugin") {
                val onCallTimeKey = AttributeKey<Long>("onCallTimeKey")
                val traceId = AttributeKey<String>("traceId")
                val spanId = AttributeKey<String>("spanId")
                observability.startTrace("traceId", "spanName", mapOf())
                on(SendingRequest) { request, _ ->
                    val onCallTime = Clock.System.now().toEpochMilliseconds()
                    request.attributes.put(onCallTimeKey, onCallTime)
                    request.attributes.put(traceId, onCallTime)
                }

                onResponse { response ->
                    val onCallTime = response.call.attributes[onCallTimeKey]
                    val onCallReceiveTime = Clock.System.now().toEpochMilliseconds()
                    println("Read response delay (ms): ${onCallReceiveTime - onCallTime}")
                }
            }
            install(responseTimePlugin)
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    },
                )
            }
            install(Logging) {
                logger = object: io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        log.d(message)
                    }
                }
                level = LogLevel.ALL
            }
        }
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
