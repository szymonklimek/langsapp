package com.langsapp.home

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.StateManager
import com.langsapp.home.welcome.WelcomeSlide
import com.langsapp.identity.IdentityState
import com.langsapp.identity.auth.AuthConfig

class HomeStateManager(
    welcomeSlides: List<WelcomeSlide>,
    authConfigProvider: () -> AuthConfig,
    homeRepository: HomeRepository,
) : StateManager<HomeState, HomeAction>(
    initialState = if (!homeRepository.hasShownWelcome()) HomeState.Welcome(welcomeSlides) else HomeState.Loading,
    initialSideEffects = if (!homeRepository.hasShownWelcome()) null else ArrayDeque(listOf(HomeSideEffect.LoadHomeDataSideEffect)),
    handleAction = { currentState, action ->
        when (action) {
            HomeAction.SkipTapped -> {
                homeRepository.setHasShownWelcome(true)
                ActionResult(
                    newState = HomeState.Loading,
                    sideEffects = ArrayDeque(listOf(HomeSideEffect.LoadHomeDataSideEffect)),
                )
            }

            HomeAction.SignUpFinished -> ActionResult()

            is HomeAction.HomeDataLoaded -> ActionResult(
                newState = HomeState.Loaded,
            )

            HomeAction.SignUpTapped -> ActionResult(
                sideEffects = ArrayDeque(
                    listOf(
                        HomeNavigationSideEffect.SignUp(
                            authConfigProvider(),
                        ),
                    ),
                ),
            )
        }
    },
    handleSideEffect = { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.LoadHomeDataSideEffect ->
                homeRepository
                    .loadHomeScreen()
                    .let {
                        HomeAction.HomeDataLoaded
                    }

            else -> null
        }
    },
    onExternalStateCoordination = { (newState, previousState, _) ->
        when {
            // Identity signed in for the first time
            newState is IdentityState.SignedIn && previousState !is IdentityState.SignedIn ->
                HomeAction.SignUpFinished

            else -> null
        }
    },
)
