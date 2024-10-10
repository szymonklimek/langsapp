package com.langsapp.data

import com.langsapp.config.Log
import com.langsapp.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MockUserProfileService : UserProfileService {
    private val userProfiles = mutableMapOf<String, UserProfile>()

    override suspend fun createUserProfile(accessToken: String, userId: String) {
        delay(500)
        userProfiles[accessToken] = UserProfile()
    }

    override suspend fun getUserProfile(accessToken: String): UserProfile? =
        withContext(Dispatchers.IO) {
            Log.d("$this getUserProfile")
            delay(500)
            userProfiles[accessToken]
        }

    override suspend fun upsertUserProperties(
        accessToken: String,
        properties: List<UserProfileService.UserProperty>,
    ) =
        userProfiles[accessToken]?.let { profile ->
            Log.d("$this upsertUserProperties, properties: $properties")
            delay(500)
            properties.forEach {
                userProfiles[accessToken] = when (it) {
                    is UserProfileService.UserProperty.LanguageSetting -> profile.copy(languageSetting = it.setting)
                    is UserProfileService.UserProperty.Username -> profile.copy(username = it.username)
                }
            }
        } ?: error("Profile not created")
}
