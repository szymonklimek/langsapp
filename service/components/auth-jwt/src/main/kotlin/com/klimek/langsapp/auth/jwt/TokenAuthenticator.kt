package com.klimek.langsapp.auth.jwt

import arrow.core.Either

interface TokenAuthenticator {
    fun authenticate(token: Token): Either<AuthenticationError, AuthenticatedUser>
}
