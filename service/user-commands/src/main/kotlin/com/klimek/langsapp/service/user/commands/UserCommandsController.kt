package com.klimek.langsapp.service.user.commands

import com.klimek.langsapp.auth.jwt.AuthenticationError
import com.klimek.langsapp.auth.jwt.Token
import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.user.commands.generated.CreateUserRequest
import com.klimek.langsapp.service.user.commands.generated.UpdateUserRequest
import com.klimek.langsapp.service.user.commands.generated.apis.UserApi
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserCommandsController(
    private val tokenAuthenticator: TokenAuthenticator,
    private val userCommandsService: UserCommandsService,
) : UserApi {

    override suspend fun createUser(
        authorization: String,
        createUserRequest: CreateUserRequest,
        clientDeviceId: String?,
        clientDeviceSystemName: String?,
        clientDeviceSystemVersion: String?,
        clientDeviceModel: String?,
        clientDeviceManufacturer: String?,
        clientAppId: String?,
        clientAppVersion: String?,
    ): ResponseEntity<Unit> = tokenAuthenticator.authenticate(Token(authorization))
        .fold(
            ifLeft = { it.handleAuthenticationError() },
            ifRight = { authenticatedUser ->
                userCommandsService.createUser(
                    userId = authenticatedUser.userId,
                    userRequest = createUserRequest,
                ).fold(
                    ifLeft = {
                        when (it) {
                            is UserCommandsService.Companion.Error.UserAlreadyExists ->
                                ResponseEntity.status(HttpStatus.CONFLICT).build()

                            is UserCommandsService.Companion.Error.UserNameAlreadySelected ->
                                ResponseEntity.badRequest().build()

                            else -> ResponseEntity.internalServerError().build()
                        }
                    },
                    ifRight = {
                        ResponseEntity.ok(it)
                    },
                )
            },
        )

    override suspend fun updateUser(
        authorization: String,
        updateUserRequest: UpdateUserRequest,
        clientDeviceId: String?,
        clientDeviceSystemName: String?,
        clientDeviceSystemVersion: String?,
        clientDeviceModel: String?,
        clientDeviceManufacturer: String?,
        clientAppId: String?,
        clientAppVersion: String?,
    ): ResponseEntity<Unit> = tokenAuthenticator.authenticate(Token(authorization))
        .fold(
            ifLeft = { it.handleAuthenticationError() },
            ifRight = { authenticatedUser ->
                userCommandsService.updateUser(
                    userId = authenticatedUser.userId,
                    updateUserRequest = updateUserRequest,
                ).fold(
                    ifLeft = {
                        when (it) {
                            is UserCommandsService.Companion.Error.UserNotFound ->
                                ResponseEntity.notFound().build()

                            is UserCommandsService.Companion.Error.UserNameAlreadySelected ->
                                ResponseEntity.badRequest().build()

                            else -> ResponseEntity.internalServerError().build()
                        }
                    },
                    ifRight = {
                        ResponseEntity.ok(it)
                    },
                )
            },
        )

    private fun <T> AuthenticationError.handleAuthenticationError(): ResponseEntity<T> = when (this) {
        is AuthenticationError.InvalidConfiguration -> ResponseEntity.internalServerError().build()
        else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
