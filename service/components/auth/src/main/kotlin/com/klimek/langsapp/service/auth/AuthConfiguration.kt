package com.klimek.langsapp.service.auth

import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.config.ConfigProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfiguration {

    @Bean
    fun tokenAuthenticator(configProvider: ConfigProvider): TokenAuthenticator =
        configProvider.getValue("auth.implementation").let {
            when (it) {
                "FAKE" -> FakeTokenAuthenticator

                "FIREBASE" -> FirebaseTokenAuthenticator(
                    x509Certificate = configProvider.getValue("auth.certificate.x509"),
                    privateKey = configProvider.getValue("auth.private.key"),
                    privateKeyId = configProvider.getValue("auth.private.key.id"),
                )

                else -> error("Invalid auth implementation: $it")
            }
        }
}
