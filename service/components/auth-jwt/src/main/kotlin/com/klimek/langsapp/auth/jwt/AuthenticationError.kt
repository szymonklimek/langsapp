package com.klimek.langsapp.auth.jwt

sealed class AuthenticationError(val message: String = "") {

    data class TokenExpired(val errorMessage: String = "") : AuthenticationError(errorMessage)

    data class InvalidToken(val errorMessage: String = "") : AuthenticationError(errorMessage)

    /**
     * Indicates that configuration of JWT library or algorithm is incorrect
     */
    data class InvalidConfiguration(val errorMessage: String = "") : AuthenticationError(errorMessage)
}
