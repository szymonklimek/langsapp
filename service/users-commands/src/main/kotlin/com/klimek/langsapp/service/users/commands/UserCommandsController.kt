package com.klimek.langsapp.service.users.commands

import com.klimek.langsapp.auth.jwt.AuthenticationError
import com.klimek.langsapp.auth.jwt.Token
import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.users.generated.UpsertUserRequest
import com.klimek.langsapp.service.users.generated.User
import com.klimek.langsapp.service.users.generated.apis.UserApi
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserCommandsController(
    private val tokenAuthenticator: TokenAuthenticator
) : UserApi {

    override suspend fun updateUserProperties(
        authorization: String,
        requestBody: Map<String, String>,
        clientDeviceId: String?,
        clientDeviceSystemName: String?,
        clientDeviceSystemVersion: String?,
        clientDeviceModel: String?,
        clientDeviceManufacturer: String?,
        clientAppId: String?,
        clientAppVersion: String?
    ) = tokenAuthenticator.authenticate(Token(authorization))
        .fold(
            ifLeft = { it.handleAuthenticationError() },
            ifRight = { ResponseEntity.ok(requestBody) }
        )

    override suspend fun upsertUser(
        authorization: String,
        upsertUserRequest: UpsertUserRequest,
        clientDeviceId: String?,
        clientDeviceSystemName: String?,
        clientDeviceSystemVersion: String?,
        clientDeviceModel: String?,
        clientDeviceManufacturer: String?,
        clientAppId: String?,
        clientAppVersion: String?
    ): ResponseEntity<User> = tokenAuthenticator.authenticate(Token(authorization))
        .fold(
            ifLeft = { it.handleAuthenticationError() },
            ifRight = { ResponseEntity.ok(User(id = it.userId, name = upsertUserRequest.name)) }
        )

    private fun <T> AuthenticationError.handleAuthenticationError(): ResponseEntity<T> = when (this) {
        is AuthenticationError.InvalidConfiguration -> ResponseEntity.internalServerError().build()
        else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
