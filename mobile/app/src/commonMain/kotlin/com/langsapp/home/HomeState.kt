package com.langsapp.home

import com.langsapp.architecture.State
import com.langsapp.home.welcome.WelcomeSlide

sealed class HomeState : State {
    data class Welcome(val slides: List<WelcomeSlide>) : HomeState()
    data object Loading : HomeState()
    data class Loaded(
        val loadedSuccessfully: Boolean,
        val userIdentity: UserIdentity?,
        val chosenLanguages: List<String>,
    ) : HomeState()

    data class UnitsSelection(
        val loaded: Loaded,
        val isLoading: Boolean,
    ) : HomeState()
}
