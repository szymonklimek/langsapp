package com.klimek.langsapp.service.auth

import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.auth.certificates.JWK
import com.klimek.langsapp.service.auth.certificates.KeysResponse
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfiguration {

    @Value("\${auth.implementation}")
    var authImplementation: String? = null

    @Value("\${auth.keycloak.base.url}")
    var keycloakBaseUrl: String? = null

    @Value("\${auth.keycloak.realm}")
    var keycloakRealm: String? = null

    @RegisterReflectionForBinding(
        classes = [
            KeysResponse::class,
            JWK::class,
        ],
    )
    @Bean
    fun tokenAuthenticator(): TokenAuthenticator =
        when (authImplementation) {
            "FAKE" -> FakeTokenAuthenticator

            "KEYCLOAK" -> KeyCloakTokenAuthenticator(
                baseUrl = keycloakBaseUrl!!,
                realm = keycloakRealm!!,
            )

            else -> error("Invalid auth implementation: $authImplementation")
        }
}
