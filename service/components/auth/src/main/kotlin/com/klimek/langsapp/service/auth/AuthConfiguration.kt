package com.klimek.langsapp.service.auth

import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.auth.certificates.JWK
import com.klimek.langsapp.service.auth.certificates.KeysResponse
import com.klimek.langsapp.service.config.ConfigProvider
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfiguration {

    @RegisterReflectionForBinding(
        classes = [
            KeysResponse::class,
            JWK::class,
        ],
    )
    @Bean
    fun tokenAuthenticator(configProvider: ConfigProvider): TokenAuthenticator =
        configProvider.getValue("auth.implementation").let {
            when (it) {
                "FAKE" -> FakeTokenAuthenticator

                "KEYCLOAK" -> KeyCloakTokenAuthenticator(
                    baseUrl = configProvider.getValue("auth.keycloak.url"),
                    realm = configProvider.getValue("auth.keycloak.realm"),
                )

                else -> error("Invalid auth implementation: $it")
            }
        }
}
