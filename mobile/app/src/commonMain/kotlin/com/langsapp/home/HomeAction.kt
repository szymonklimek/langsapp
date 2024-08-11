package com.langsapp.home

import com.langsapp.architecture.Action
import com.langsapp.common.CommonResult
import com.langsapp.home.onboarding.OnBoardingInfo
import com.langsapp.home.onboarding.UserProfileInfo

sealed class HomeAction : Action {
    data object SkipTapped : HomeAction()
    data object SignUpTapped : HomeAction()
    data object SignUpFinished : HomeAction()
    data object UpsertProfileTapped : HomeAction()
    data object UpsertProfileFinished : HomeAction()
    data object SelectLanguagesTapped : HomeAction()
    data object SelectLanguagesFinished : HomeAction()
    data object DownloadContentTapped : HomeAction()
    data object DownloadContentFinished : HomeAction()
    data class HomeDataLoaded(
        val userProfileInfo: CommonResult<UserProfileInfo, Unit>,
        val onBoardingInfo: CommonResult<OnBoardingInfo, Unit>,
    ) : HomeAction()
}
