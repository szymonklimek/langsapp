package com.langsapp.home

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.StateManager
import com.langsapp.config.Log
import com.langsapp.content.ManageContentState
import com.langsapp.home.onboarding.UserProfileInfo
import com.langsapp.home.welcome.WelcomeSlide
import com.langsapp.identity.IdentityState
import com.langsapp.identity.auth.AuthConfig
import com.langsapp.settings.language.LanguageSettingsState
import com.langsapp.userprofile.upsert.UpsertProfileState

class HomeStateManager(
    welcomeSlides: List<WelcomeSlide>,
    authConfigProvider: () -> AuthConfig,
    devOptionsEnabled: Boolean,
    homeRepository: HomeRepository,
) : StateManager<HomeState, HomeAction>(
    initialState = if (!homeRepository.hasShownWelcome()) {
        HomeState.Welcome(welcomeSlides, devOptionsEnabled)
    } else {
        HomeState.Loading
    },
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

            HomeAction.SignUpFinished -> {
                if (currentState is HomeState.Loaded &&
                    currentState.userProfileInfo.getOrNull() is UserProfileInfo.Anonymous
                ) {
                    // Refresh home data when user really signed in
                    ActionResult(
                        newState = HomeState.Loading,
                        sideEffects = ArrayDeque(
                            listOf(
                                HomeSideEffect.LoadHomeDataSideEffect,
                            ),
                        ),
                    )
                } else {
                    ActionResult()
                }
            }

            HomeAction.UpsertProfileFinished,
            HomeAction.SelectLanguagesFinished,
            HomeAction.DownloadContentFinished,
            -> ActionResult(
                sideEffects = ArrayDeque(
                    listOf(
                        HomeSideEffect.LoadHomeDataSideEffect,
                    ),
                ),
            )

            is HomeAction.HomeDataLoaded -> ActionResult(
                newState = HomeState.Loaded(action.userProfileInfo, action.onBoardingInfo, devOptionsEnabled),
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

            HomeAction.SelectLanguagesTapped -> ActionResult(sideEffects = ArrayDeque(listOf(HomeNavigationSideEffect.SelectLanguages)))
            HomeAction.UpsertProfileTapped -> {
                when (currentState) {
                    is HomeState.Loaded -> {
                        val userProfile = currentState.userProfileInfo.getOrNull()
                        if (userProfile == null) {
                            Log.e("Trying to open create profile without profile data")
                            ActionResult()
                        } else {
                            ActionResult(
                                sideEffects = ArrayDeque(listOf(HomeNavigationSideEffect.CreateProfile(userProfile))),
                            )
                        }
                    }

                    else -> {
                        Log.e("Trying to open create profile from invalid state: $currentState")
                        ActionResult()
                    }
                }
            }

            HomeAction.DownloadContentTapped ->
                when (currentState) {
                    is HomeState.Loaded -> {
                        val languageSetting =
                            currentState.userProfileInfo.getOrNull()?.languageSetting()
                        if (languageSetting == null) {
                            Log.e("Trying to open content download without language setting")
                            ActionResult()
                        } else {
                            ActionResult(
                                sideEffects = ArrayDeque(
                                    listOf(HomeNavigationSideEffect.ManageContent(languageSetting)),
                                ),
                            )
                        }
                    }

                    else -> {
                        Log.e("Trying to open content download from invalid state: $currentState")
                        ActionResult()
                    }
                }

            HomeAction.DevOptionsTapped -> ActionResult(
                sideEffects = ArrayDeque(listOf(HomeNavigationSideEffect.DevOptions)),
            )
        }
    },
    handleSideEffect = { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.LoadHomeDataSideEffect ->
                homeRepository
                    .loadHomeScreen()
                    .let {
                        HomeAction.HomeDataLoaded(
                            userProfileInfo = it.userProfileInfo,
                            onBoardingInfo = it.onBoardingInfo,
                        )
                    }

            else -> null
        }
    },
    onExternalStateCoordination = { (newState, previousState, _) ->
        when {
            // Identity signed in for the first time
            newState is IdentityState.SignedIn && previousState !is IdentityState.SignedIn ->
                HomeAction.SignUpFinished
            // Navigating back from language settings
            newState is HomeState && previousState is LanguageSettingsState ->
                HomeAction.SelectLanguagesFinished

            newState is HomeState && previousState is UpsertProfileState ->
                HomeAction.UpsertProfileFinished

            newState is HomeState && previousState is ManageContentState.Loaded ->
                HomeAction.DownloadContentFinished

            else -> null
        }
    },
)
