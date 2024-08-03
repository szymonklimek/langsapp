package com.klimek.langsapp.service.auth

import arrow.core.Either
import arrow.core.right
import com.klimek.langsapp.auth.jwt.AuthenticatedUser
import com.klimek.langsapp.auth.jwt.AuthenticationError
import com.klimek.langsapp.auth.jwt.Token
import com.klimek.langsapp.auth.jwt.TokenAuthenticator

object FakeTokenAuthenticator : TokenAuthenticator {
    override fun authenticate(token: Token): Either<AuthenticationError, AuthenticatedUser> =
        AuthenticatedUser(userId = token.value).right()
}
