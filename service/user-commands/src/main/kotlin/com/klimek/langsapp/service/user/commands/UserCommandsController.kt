package com.klimek.langsapp.service.user.commands

import com.klimek.langsapp.auth.jwt.AuthenticationError
import com.klimek.langsapp.auth.jwt.Token
import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.user.commands.generated.UserRequest
import com.klimek.langsapp.service.user.commands.generated.UserResponse
import com.klimek.langsapp.service.user.commands.generated.apis.UserApi
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserCommandsController(
    private val tokenAuthenticator: TokenAuthenticator
) : UserApi {

    override suspend fun createUser(
        authorization: String,
        userRequest: UserRequest,
        clientDeviceId: String?,
        clientDeviceSystemName: String?,
        clientDeviceSystemVersion: String?,
        clientDeviceModel: String?,
        clientDeviceManufacturer: String?,
        clientAppId: String?,
        clientAppVersion: String?
    ): ResponseEntity<UserResponse> = tokenAuthenticator.authenticate(Token(authorization))
        .fold(
            ifLeft = { it.handleAuthenticationError() },
            ifRight = { ResponseEntity.ok(UserResponse(id = it.userId, name = userRequest.name)) }
        )

    override suspend fun updateUser(
        authorization: String,
        userRequest: UserRequest,
        clientDeviceId: String?,
        clientDeviceSystemName: String?,
        clientDeviceSystemVersion: String?,
        clientDeviceModel: String?,
        clientDeviceManufacturer: String?,
        clientAppId: String?,
        clientAppVersion: String?
    ): ResponseEntity<UserResponse> = tokenAuthenticator.authenticate(Token(authorization))
        .fold(
            ifLeft = { it.handleAuthenticationError() },
            ifRight = { ResponseEntity.ok(UserResponse(id = it.userId, name = userRequest.name)) }
        )

    private fun <T> AuthenticationError.handleAuthenticationError(): ResponseEntity<T> = when (this) {
        is AuthenticationError.InvalidConfiguration -> ResponseEntity.internalServerError().build()
        else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
