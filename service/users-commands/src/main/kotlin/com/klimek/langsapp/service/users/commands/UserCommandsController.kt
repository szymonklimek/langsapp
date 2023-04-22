package com.klimek.langsapp.service.users.commands

import com.klimek.langsapp.service.users.generated.UpsertUserRequest
import com.klimek.langsapp.service.users.generated.User
import com.klimek.langsapp.service.users.generated.apis.UserApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class UserCommandsController : UserApi {

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
    ) = ResponseEntity.ok(requestBody)

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
    ) = ResponseEntity.ok(User(id = UUID.randomUUID().toString(), name = upsertUserRequest.name))
}
