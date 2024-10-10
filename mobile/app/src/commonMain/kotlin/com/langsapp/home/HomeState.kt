package com.langsapp.home

import com.langsapp.architecture.State
import com.langsapp.home.onboarding.OnBoardingInfo
import com.langsapp.home.onboarding.UserProfileInfo
import com.langsapp.home.welcome.WelcomeSlide

sealed class HomeState : State {
    data class Welcome(val slides: List<WelcomeSlide>) : HomeState()
    data object Loading : HomeState()
    data class Loaded(
        val userProfileInfo: Result<UserProfileInfo>,
        val onBoardingInfo: Result<OnBoardingInfo>,
    ) : HomeState()
}
