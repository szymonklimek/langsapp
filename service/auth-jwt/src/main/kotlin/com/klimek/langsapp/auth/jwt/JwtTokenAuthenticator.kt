package com.klimek.langsapp.auth.jwt

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException

/**
 * Implementation of [TokenAuthenticator] that works with JWT tokens (https://jwt.io/)
 * and expects identifier of the user stored in the `subject` claim
 */
open class JwtTokenAuthenticator(
    private val algorithm: Algorithm
) : TokenAuthenticator {
    override fun authenticate(token: Token): Either<AuthenticationError, AuthenticatedUser> = catch({
        JWT.require(algorithm)
            .build()
            .verify(token.value)
            .subject
            .right()
    })
    {
        when (it) {
            is TokenExpiredException -> AuthenticationError.TokenExpired(it.message ?: "").left()
            is JWTVerificationException -> AuthenticationError.InvalidToken(it.message ?: "").left()
            else -> AuthenticationError.InvalidConfiguration(it.message ?: "").left()
        }
    }
        .map { AuthenticatedUser(userId = it) }
}
