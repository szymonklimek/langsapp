package com.langsapp.home

import com.langsapp.AuthConfig
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.learn.LearnUnit

sealed class HomeNavigationSideEffect(direction: Direction) : CommonSideEffect.NavigationSideEffect(direction) {
    data class NavigateToLogin(val authConfig: AuthConfig) : HomeNavigationSideEffect(direction = Direction.FORWARD)

    data object SkipWelcome : HomeNavigationSideEffect(direction = Direction.FORWARD)

    data class Learn(val unitsToLearn: List<LearnUnit>) : HomeNavigationSideEffect(Direction.FORWARD)
}
