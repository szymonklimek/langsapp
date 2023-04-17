package com.klimek.langsapp.service.user.follow.commands

import com.klimek.langsapp.service.users.generated.FollowRequest
import com.klimek.langsapp.service.users.generated.apis.FollowApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserFollowCommandsController : FollowApi {

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
    ) = ResponseEntity.ok(Unit)

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
    ) = ResponseEntity.ok(Unit)
}
