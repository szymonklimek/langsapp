package com.langsapp.data

import com.klimek.langsapp.openapi.generated.user.commands.UserApi
import com.klimek.langsapp.openapi.generated.user.commands.model.CreateUserRequest
import com.klimek.langsapp.openapi.generated.user.commands.model.LanguageSetting
import com.klimek.langsapp.openapi.generated.user.commands.model.LanguageSettings
import com.klimek.langsapp.openapi.generated.user.commands.model.UpdateUserRequest
import com.klimek.langsapp.openapi.generated.user.profile.query.ProfileApi
import com.langsapp.config.Log
import com.langsapp.model.UserProfile
import com.langsapp.platform.AppInstallationInfo
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class RemoteUserProfileService(
    private val baseUrlProvider: () -> String,
    private val httpClientProvider: () -> HttpClient,
    private val appInstallationInfo: AppInstallationInfo,
) : UserProfileService {
    override suspend fun createUserProfile(accessToken: String, userId: String) = withContext(Dispatchers.IO) {
        Log.d("$this createUserProfile, userId: $userId")
        UserApi(baseUrlProvider(), httpClientProvider())
            .createUser(
                authorization = "Bearer $accessToken",
                createUserRequest = CreateUserRequest(id = userId),
                clientDeviceId = appInstallationInfo.installationId,
                clientAppVersion = appInstallationInfo.appVersion,
                clientAppId = appInstallationInfo.appId,
                clientDeviceModel = appInstallationInfo.deviceModel,
                clientDeviceManufacturer = appInstallationInfo.deviceManufacturer,
                clientDeviceSystemName = appInstallationInfo.deviceSystemName,
                clientDeviceSystemVersion = appInstallationInfo.deviceSystemVersion,
            )
            .body()
    }

    override suspend fun getUserProfile(accessToken: String): UserProfile? =
        withContext(Dispatchers.IO) {
            Log.d("$this getUserProfile")
            ProfileApi(baseUrlProvider(), httpClientProvider())
                .getCurrentUserProfile(
                    authorization = "Bearer $accessToken",
                    clientDeviceId = appInstallationInfo.installationId,
                    clientAppVersion = appInstallationInfo.appVersion,
                    clientAppId = appInstallationInfo.appId,
                    clientDeviceModel = appInstallationInfo.deviceModel,
                    clientDeviceManufacturer = appInstallationInfo.deviceManufacturer,
                    clientDeviceSystemName = appInstallationInfo.deviceSystemName,
                    clientDeviceSystemVersion = appInstallationInfo.deviceSystemVersion,
                )
                .body()
                .userProfile
                ?.let {
                    UserProfile(
                        it.id,
                    )
                }
        }

    override suspend fun upsertUserProperties(
        accessToken: String,
        properties: List<UserProfileService.UserProperty>,
    ) = withContext(Dispatchers.IO) {
        Log.d("$this upsertUserProperties, properties: $properties")
        UserApi(baseUrlProvider(), httpClientProvider())
            .updateUser(
                authorization = "Bearer $accessToken",
                updateUserRequest = with(properties) {
                    UpdateUserRequest(
                        username = filterIsInstance<UserProfileService.UserProperty.Username>().firstOrNull()?.username,
                        languageSettings = filterIsInstance<UserProfileService.UserProperty.LanguageSetting>()
                            .firstOrNull()?.setting?.let {
                                LanguageSettings(
                                    learnLanguage = LanguageSetting(it.learnLanguage.code),
                                    baseLanguage = LanguageSetting(it.baseLanguage.code),
                                )
                            },
                    )
                },
                clientDeviceId = appInstallationInfo.installationId,
                clientAppVersion = appInstallationInfo.appVersion,
                clientAppId = appInstallationInfo.appId,
                clientDeviceModel = appInstallationInfo.deviceModel,
                clientDeviceManufacturer = appInstallationInfo.deviceManufacturer,
                clientDeviceSystemName = appInstallationInfo.deviceSystemName,
                clientDeviceSystemVersion = appInstallationInfo.deviceSystemVersion,
            )
            .body()
    }
}
