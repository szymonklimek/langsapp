package com.klimek.langsapp.service.user.follow.commands

import com.klimek.langsapp.auth.jwt.AuthenticationError
import com.klimek.langsapp.auth.jwt.Token
import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.users.generated.FollowRequest
import com.klimek.langsapp.service.users.generated.apis.FollowApi
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserFollowCommandsController(
    private val tokenAuthenticator: TokenAuthenticator
) : FollowApi {

    override suspend fun followUser(
        authorization: String,
        followRequest: FollowRequest,
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
            ifRight = { ResponseEntity.ok(Unit) }
        )

    override suspend fun unfollowUser(
        authorization: String,
        followRequest: FollowRequest,
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
            ifRight = { ResponseEntity.ok(Unit) }
        )

    private fun <T> AuthenticationError.handleAuthenticationError(): ResponseEntity<T> = when (this) {
        is AuthenticationError.InvalidConfiguration -> ResponseEntity.internalServerError().build()
        else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
