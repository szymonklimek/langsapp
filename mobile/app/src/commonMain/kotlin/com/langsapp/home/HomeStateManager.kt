package com.langsapp.home

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.StateManager
import com.langsapp.home.welcome.WelcomeSlide

class HomeStateManager(
    welcomeSlides: List<WelcomeSlide>,
    homeRepository: HomeRepository,
) : StateManager<HomeState, HomeAction>(
    initialState = if (!homeRepository.hasShownWelcome()) HomeState.Welcome(welcomeSlides) else HomeState.Loading,
    initialSideEffects = if (!homeRepository.hasShownWelcome()) null else ArrayDeque(listOf(HomeSideEffect.FetchHomeDataSideEffect)),
    handleAction = { currentState, action ->
        when (action) {
            HomeAction.SkipTapped -> {
                homeRepository.setHasShownWelcome(true)
                ActionResult(
                    newState = HomeState.Loading,
                    sideEffects = ArrayDeque(listOf(HomeSideEffect.FetchHomeDataSideEffect)),
                )
            }

            is HomeAction.HomeDataLoaded -> ActionResult(
                newState = HomeState.Loaded,
            )
        }
    },
    handleSideEffect = { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.FetchHomeDataSideEffect -> {
                homeRepository.loadHomeScreen().fold(
                    onSuccess = { HomeAction.HomeDataLoaded },
                    onFailure = { HomeAction.HomeDataLoaded },
                )
            }
            else -> null
        }
    },
)
