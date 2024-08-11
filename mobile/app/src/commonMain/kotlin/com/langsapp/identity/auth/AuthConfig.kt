package com.langsapp.identity.auth

data class AuthConfig(
    val authorizationEndpoint: String,
    val tokenEndpoint: String,
    val registrationEndpoint: String,
    val endSessionEndpoint: String,
    val clientId: String,
    val redirectUri: String,
)
