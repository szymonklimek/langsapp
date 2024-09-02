package com.klimek.langsapp.auth.jwt

import arrow.core.Either
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.RSAKeyProvider
import java.security.cert.CertificateFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Base64

/**
 * Implementation of token authenticator that works with JWT tokens (https://jwt.io/)
 * and expects identifier of the user stored in the `subject` claim
 */
class JwtTokenAuthenticator(
    private val validTokenIssuer: String,
    private val x509CertificateProvider: (String) -> String,
) : TokenAuthenticator {

    private val algorithmCache = mutableMapOf<String, Algorithm>()

    override fun authenticate(
        token: Token,
        skipVerification: Boolean,
    ): Either<AuthenticationError, AuthenticatedUser> =
        runCatching {
            val decodedToken = JWT.decode(token.value)
            if (decodedToken.issuer != validTokenIssuer) {
                return@runCatching Either.Left(
                    AuthenticationError.InvalidToken(
                        "Invalid token issuer. " +
                            "Expected: $validTokenIssuer, but was: ${decodedToken.issuer}",
                    ),
                )
            }

            Either.Right(
                AuthenticatedUser(
                    userId =
                    if (skipVerification) {
                        decodedToken
                    } else {
                        JWT
                            .require(getOrCreateAlgorithm(decodedToken.keyId ?: ""))
                            .acceptLeeway(120)
                            .build()
                            .verify(decodedToken)
                    }
                        .subject,
                ),
            )
        }
            .getOrElse {
                Either.Left(
                    when (it) {
                        is TokenExpiredException -> AuthenticationError.TokenExpired(it.message ?: "")
                        is JWTVerificationException -> AuthenticationError.InvalidToken(it.message ?: "")
                        else -> AuthenticationError.InvalidConfiguration(it.message ?: "")
                    },
                )
            }

    private fun getOrCreateAlgorithm(publicKeyId: String) =
        if (algorithmCache[publicKeyId] != null) {
            algorithmCache[publicKeyId]
        } else {
            Algorithm.RSA256(object : RSAKeyProvider {
                override fun getPublicKeyById(keyId: String?) = CertificateFactory
                    .getInstance("X.509")
                    .generateCertificate(Base64.getDecoder().decode(x509CertificateProvider(publicKeyId)).inputStream())
                    .publicKey as RSAPublicKey

                override fun getPrivateKey(): RSAPrivateKey? = null

                override fun getPrivateKeyId(): String? = null
            })
                .also {
                    algorithmCache[publicKeyId] = it
                }
        }
}
