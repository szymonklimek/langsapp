package com.langsapp.home.onboarding

import com.langsapp.config.KeyValueStorage
import com.langsapp.config.Log
import com.langsapp.data.UserProfileService
import com.langsapp.identity.IdentityState
import com.langsapp.identity.TokenExpiredException
import com.langsapp.model.LanguageSetting
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserProfileRepository(
    private val identityStateProvider: () -> IdentityState,
    private val profileService: UserProfileService,
    private val keyValueStorage: KeyValueStorage,
) {
    suspend fun provideProfileInfo(): Result<UserProfileInfo> =
        // Load profile stored locally
        keyValueStorage
            .get(getUserStorageKey(userId = identityStateProvider().userId))
            ?.let {
                runCatching { Json.decodeFromString<UserProfileInfo>(it) }
                    .onFailure { Log.e("Failed to parse ProfileInfo. Reason: $it") }
                    .getOrNull()
            }
            ?.let {
                Result.success(it)
            }
            ?: run {
                when (val identityState = identityStateProvider()) {
                    // Create new anonymous user profile
                    is IdentityState.AnonymousUser -> {
                        Log.d("Creating new anonymous profile for user id: ${identityState.userId}")
                        Result.success(UserProfileInfo.Anonymous(null))
                    }

                    // Token expired, user has to sign in again
                    is IdentityState.TokenExpired -> Result.failure(Throwable("Token expired"))

                    is IdentityState.SignedIn -> {
                        Log.d("User signed in (id: ${identityState.userId}). Downloading profile from server")
                        runCatching { identityState.readRemoteProfile() }
                            .mapCatching {
                                Log.d("Downloaded profile from server: $it")
                                it ?: createNewUserProfile(identityState)
                            }
                    }
                }
            }
                .onSuccess {
                    keyValueStorage.set(getUserStorageKey(identityStateProvider().userId), Json.encodeToString(it))
                }

    suspend fun updateLanguageSetting(languageSetting: LanguageSetting): Result<Unit> =
        provideProfileInfo()
            .mapCatching { profileInfo ->
                when (profileInfo) {
                    is UserProfileInfo.Anonymous -> profileInfo.copy(languageSetting = languageSetting)
                    is UserProfileInfo.SignedIn -> {
                        when (val identityState = identityStateProvider()) {
                            is IdentityState.AnonymousUser -> error("Unable to update username for anonymous user.")
                            is IdentityState.TokenExpired -> throw TokenExpiredException()

                            is IdentityState.SignedIn ->
                                profileService.upsertUserProperties(
                                    identityState.accessToken,
                                    listOf(UserProfileService.UserProperty.LanguageSetting(languageSetting)),
                                )
                                    .let {
                                        profileInfo.copy(languageSetting = languageSetting)
                                    }
                        }
                    }
                }
            }
            .onSuccess {
                keyValueStorage.set(getUserStorageKey(identityStateProvider().userId), Json.encodeToString(it))
            }
            .map { }

    suspend fun updateUsername(username: String): Result<Unit> =
        provideProfileInfo()
            .mapCatching { profileInfo ->
                when (profileInfo) {
                    is UserProfileInfo.Anonymous -> error("Unable to update username for anonymous user.")
                    is UserProfileInfo.SignedIn -> {
                        when (val identityState = identityStateProvider()) {
                            is IdentityState.SignedIn -> profileService.upsertUserProperties(
                                identityState.accessToken,
                                listOf(UserProfileService.UserProperty.Username(username)),
                            )
                                .let {
                                    profileInfo.copy(username = username)
                                }

                            is IdentityState.AnonymousUser -> error("Unable to update username for anonymous user.")
                            is IdentityState.TokenExpired -> throw TokenExpiredException()
                        }
                    }
                }
            }
            .onSuccess {
                keyValueStorage.set(
                    getUserStorageKey(identityStateProvider().userId),
                    Json.encodeToString(it as UserProfileInfo)
                )
            }
            .map { }

    private suspend fun createNewUserProfile(identityState: IdentityState.SignedIn): UserProfileInfo.SignedIn {
        Log.d("Creating new user profile for user id: ${identityState.userId}")
        return profileService
            .createUserProfile(identityState.accessToken, identityState.userId)
            .let {
                UserProfileInfo.SignedIn(null, getLastAnonymousUserLanguageSetting())
            }
            .let {
                if (it.languageSetting != null) {
                    Log.d("Updating fresh user profile with language setting from anonymous user")
                    updateLanguageSetting(it.languageSetting)
                    it
                } else {
                    it
                }
            }
    }

    private fun getLastAnonymousUserLanguageSetting() =
        keyValueStorage
            .getAll()
            .mapNotNull {
                Log.d("$it")
                if (it.key.contains(PROFILE_INFO_KEY)) {
                    runCatching {
                        Json.decodeFromString<UserProfileInfo>(it.value)
                    }
                        .getOrNull()
                } else {
                    null
                }
            }
            .filterIsInstance<UserProfileInfo.Anonymous>()
            .lastOrNull()
            ?.languageSetting

    private suspend fun IdentityState.SignedIn.readRemoteProfile() =
        profileService
            .getUserProfile(accessToken)
            ?.let {
                UserProfileInfo.SignedIn(
                    username = it.username,
                    languageSetting = it.languageSetting,
                )
            }

    companion object {
        private const val PROFILE_INFO_KEY = "profile-info"
        private fun getUserStorageKey(userId: String) = "$userId-$PROFILE_INFO_KEY"
    }
}
