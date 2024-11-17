package com.langsapp.home

import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.SideEffect
import com.langsapp.home.onboarding.UserProfileInfo
import com.langsapp.identity.auth.AuthConfig
import com.langsapp.model.LanguageSetting

sealed interface HomeSideEffect {
    data object LoadHomeDataSideEffect : SideEffect
}

sealed class HomeNavigationSideEffect(direction: Direction) : CommonSideEffect.NavigationSideEffect(direction) {
    data class SignUp(val authConfig: AuthConfig) : HomeNavigationSideEffect(Direction.FORWARD)
    data object SelectLanguages : HomeNavigationSideEffect(Direction.FORWARD)
    data class CreateProfile(
        val userProfileInfo: UserProfileInfo,
    ) : HomeNavigationSideEffect(Direction.FORWARD)
    data class ManageContent(
        val languageSetting: LanguageSetting,
    ) : HomeNavigationSideEffect(Direction.FORWARD)
    data object DevOptions : HomeNavigationSideEffect(Direction.FORWARD)
}
