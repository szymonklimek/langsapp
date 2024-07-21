package com.langsapp.home

import com.langsapp.architecture.Action
import com.langsapp.learn.LearnUnit

sealed class HomeAction : Action {
    data object SkipTapped : HomeAction()
    data object LoginTapped : HomeAction()
    data class UserSignedIn(val accessToken: String) : HomeAction()
    data object UserSignedOut : HomeAction()
    data class HomeDataLoaded(
        val loadedSuccessfully: Boolean,
        val userIdentity: UserIdentity?,
        val chosenLanguages: List<String>,
    ) : HomeAction()
    data object BackTapped : HomeAction()
    data object LearnTapped : HomeAction()
    data object RepeatTapped : HomeAction()
    data class UnitsNumberSubmitted(val count: Int) : HomeAction()
    data class UnitsLoadSuccess(val units: List<LearnUnit>) : HomeAction()
    data class UnitsLoadFailure(val units: List<String>) : HomeAction()
    data object LanguageSettingsTapped : HomeAction()
    data object UnitsTapped : HomeAction()
    data object LoadingProfileRequested : HomeAction()
}
