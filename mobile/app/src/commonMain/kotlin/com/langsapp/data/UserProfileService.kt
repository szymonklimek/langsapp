package com.langsapp.data

import com.langsapp.model.UserProfile

interface UserProfileService {

    sealed class UserProperty {
        data class LanguageSetting(val setting: com.langsapp.model.LanguageSetting) : UserProperty()
        data class Username(val username: String) : UserProperty()
    }

    suspend fun createUserProfile(
        accessToken: String,
        userId: String,
    )

    suspend fun getUserProfile(
        accessToken: String,
    ): UserProfile?

    suspend fun upsertUserProperties(
        accessToken: String,
        properties: List<UserProperty>,
    )
}
