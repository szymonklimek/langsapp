package com.langsapp.home

import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.SideEffect
import com.langsapp.identity.auth.AuthConfig

sealed interface HomeSideEffect {
    data object LoadHomeDataSideEffect : SideEffect
}

sealed class HomeNavigationSideEffect(direction: Direction) : CommonSideEffect.NavigationSideEffect(direction) {
    data class SignUp(val authConfig: AuthConfig) : HomeNavigationSideEffect(Direction.FORWARD)
}
