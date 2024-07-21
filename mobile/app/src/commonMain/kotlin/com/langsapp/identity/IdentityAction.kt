package com.langsapp.identity

import com.langsapp.architecture.Action

sealed class IdentityAction : Action {
    data class UserSignedIn(
        val accessToken: String,
        val refreshToken: String,
        val userId: String,
        val accessTokenExpiresAtTimestampMs: Long,
    ) : IdentityAction()

    data object UserSignedOut : IdentityAction()
}
