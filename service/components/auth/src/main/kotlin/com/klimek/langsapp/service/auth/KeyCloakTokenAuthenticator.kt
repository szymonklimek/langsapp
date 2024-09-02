package com.klimek.langsapp.service.auth

import arrow.core.Either
import com.klimek.langsapp.auth.jwt.AuthenticatedUser
import com.klimek.langsapp.auth.jwt.AuthenticationError
import com.klimek.langsapp.auth.jwt.JwtTokenAuthenticator
import com.klimek.langsapp.auth.jwt.Token
import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.auth.certificates.KeyCloakAuthenticationCertificateProvider

class KeyCloakTokenAuthenticator(
    baseUrl: String,
    realm: String,
) : TokenAuthenticator {

    private val keyCloakAuthenticationCertificateProvider = KeyCloakAuthenticationCertificateProvider(
        baseUrl = baseUrl,
        realm = realm,
    )

    private val jwtTokenAuthenticator = JwtTokenAuthenticator(
        validTokenIssuer = "$baseUrl/realms/$realm",
        x509CertificateProvider = { keyCloakAuthenticationCertificateProvider.invoke(it) },
    )

    override fun authenticate(
        token: Token,
        skipVerification: Boolean,
    ): Either<AuthenticationError, AuthenticatedUser> = jwtTokenAuthenticator.authenticate(token, skipVerification)
}
