package com.langsapp.android.identity.auth

sealed class AuthResult {
    data class SignedIn(
        val accessToken: String,
        val refreshToken: String,
        val userId: String,
        val accessTokenExpiresAtTimestampMs: Long,
    ) : AuthResult()

    data object Cancelled : AuthResult()

    data object Error : AuthResult()
}
