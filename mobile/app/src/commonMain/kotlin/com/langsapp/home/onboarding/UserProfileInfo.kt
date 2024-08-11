package com.langsapp.home.onboarding

import com.langsapp.model.LanguageSetting
import kotlinx.serialization.Serializable

@Serializable
sealed class UserProfileInfo {

    @Serializable
    data class SignedIn(
        val username: String?,
        val languageSetting: LanguageSetting?,
    ) : UserProfileInfo()

    @Serializable
    data class Anonymous(
        val languageSetting: LanguageSetting?,
    ) : UserProfileInfo()

    fun languageSetting() = when (this) {
        is Anonymous -> languageSetting
        is SignedIn -> languageSetting
    }
}
