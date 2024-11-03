package com.klimek.langsapp.service.user.profile

import com.klimek.langsapp.auth.jwt.AuthenticationError
import com.klimek.langsapp.auth.jwt.Token
import com.klimek.langsapp.auth.jwt.TokenAuthenticator
import com.klimek.langsapp.service.user.profile.query.generated.ProfileResponse
import com.klimek.langsapp.service.user.profile.query.generated.apis.ProfileApi
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserProfileQueryController(
    private val tokenAuthenticator: TokenAuthenticator,
    private val userProfileQueryService: UserProfileQueryService,
) : ProfileApi {

    override suspend fun getCurrentUserProfile(
        authorization: String,
        clientDeviceId: String?,
        clientDeviceSystemName: String?,
        clientDeviceSystemVersion: String?,
        clientDeviceModel: String?,
        clientDeviceManufacturer: String?,
        clientAppId: String?,
        clientAppVersion: String?,
    ) = tokenAuthenticator.authenticate(Token(authorization))
        .fold(
            ifLeft = { it.handleAuthenticationError() },
            ifRight = { authenticatedUser ->
                userProfileQueryService.getUserProjection(userId = authenticatedUser.userId)
                    .fold(
                        ifLeft = {
                            ResponseEntity.internalServerError().build()
                        },
                        ifRight = {
                            ResponseEntity.ok(
                                ProfileResponse(userProfile = it),
                            )
                        },
                    )
            },
        )

    private fun <T> AuthenticationError.handleAuthenticationError(): ResponseEntity<T> = when (this) {
        is AuthenticationError.InvalidConfiguration -> ResponseEntity.internalServerError().build()
        else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}
