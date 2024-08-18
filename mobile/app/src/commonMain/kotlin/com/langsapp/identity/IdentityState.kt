package com.langsapp.identity

import com.langsapp.architecture.State
import kotlinx.serialization.Serializable

@Serializable
sealed interface IdentityState : State {
    val userId: String

    @Serializable
    data class SignedIn(
        override val userId: String,
        val accessToken: String,
        val refreshToken: String,
        val accessTokenExpiresAtTimestampMs: Long,
    ) : IdentityState

    @Serializable
    data class TokenExpired(override val userId: String) : IdentityState

    @Serializable
    data class AnonymousUser(override val userId: String) : IdentityState
}
