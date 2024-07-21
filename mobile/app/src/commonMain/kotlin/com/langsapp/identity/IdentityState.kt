package com.langsapp.identity

import com.langsapp.architecture.State
import kotlinx.serialization.Serializable

sealed class IdentityState : State {

    @Serializable
    data class SignedIn(
        val accessToken: String,
        val refreshToken: String,
        val userId: String,
        val accessTokenExpiresAtTimestampMs: Long,
    ) : IdentityState()

    data object TokenExpired : IdentityState()

    data class AnonymousUser(
        val userId: String,
    ) : IdentityState()
}
