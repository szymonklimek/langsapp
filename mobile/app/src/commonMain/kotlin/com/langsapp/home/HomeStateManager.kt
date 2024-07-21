package com.langsapp.home

import com.langsapp.AuthConfig
import com.langsapp.ContentRepository
import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.StateManager
import com.langsapp.architecture.StateTransition
import com.langsapp.home.welcome.WelcomeSlide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class HomeStateManager(
    shouldShowWelcome: () -> Boolean,
    welcomeSlides: List<WelcomeSlide>,
    homeRepository: HomeRepository,
    contentRepository: ContentRepository,
) : StateManager<HomeState, HomeAction>(
    initialState = if (shouldShowWelcome()) HomeState.Welcome(welcomeSlides) else HomeState.Loading,
    initialSideEffects = if (shouldShowWelcome()) null else ArrayDeque(listOf(HomeSideEffect.FetchHomeDataSideEffect)),
    handleAction = { previousState, action ->
        when (action) {
            HomeAction.LoginTapped -> ActionResult(
                sideEffects = listOf(
                    HomeNavigationSideEffect.NavigateToLogin(
                        AuthConfig(
                            authorizationEndpoint = "https://auth.sklimek.com/realms/langsapp/protocol/openid-connect/auth",
                            tokenEndpoint = "https://auth.sklimek.com/realms/langsapp/protocol/openid-connect/token",
                            registrationEndpoint = "https://auth.sklimek.com/realms/langsapp/clients-registrations/openid-connect",
                            endSessionEndpoint = "https://auth.sklimek.com/realms/langsapp/protocol/openid-connect/logout",
                            clientId = "langsapp_android_app",
                            redirectUri = "langsapp://langs.app",
                        ),
                    ),
                ),
            )

            is HomeAction.UserSignedIn -> {
                ActionResult(sideEffects = ArrayDeque(listOf(HomeSideEffect.FetchHomeDataSideEffect)))
            }

            HomeAction.SkipTapped -> ActionResult(
                newState = HomeState.Loading,
                sideEffects = ArrayDeque(listOf(HomeSideEffect.FetchHomeDataSideEffect)),
            )

            is HomeAction.HomeDataLoaded -> ActionResult(
                newState = HomeState.Loaded(
                    userIdentity = action.userIdentity,
                    chosenLanguages = action.chosenLanguages,
                    loadedSuccessfully = action.loadedSuccessfully,
                ),
            )

            HomeAction.LearnTapped ->
                if (previousState is HomeState.Loaded) {
                    ActionResult(
                        newState = HomeState.UnitsSelection(previousState, isLoading = false),
                        stateTransition = object : StateTransition {}
                    )
                } else {
                    ActionResult()
                }

            HomeAction.BackTapped -> when (previousState) {
                is HomeState.UnitsSelection -> ActionResult(previousState.loaded)
                // TODO Put app on background here
                else -> ActionResult(sideEffects = emptyList())
            }

            HomeAction.LanguageSettingsTapped -> ActionResult()
            HomeAction.LoadingProfileRequested -> ActionResult()
            HomeAction.RepeatTapped -> ActionResult()
            is HomeAction.UnitsNumberSubmitted ->
                ActionResult(sideEffects = listOf(HomeSideEffect.FetchLearnUnitsSideEffect(action.count)))

            is HomeAction.UnitsLoadFailure -> ActionResult()
            is HomeAction.UnitsLoadSuccess -> ActionResult(
                sideEffects = listOf(
                    HomeNavigationSideEffect.Learn(action.units),
                ),
            )

            HomeAction.UnitsTapped -> ActionResult()
            HomeAction.UserSignedOut -> TODO()
        }
    },
    handleSideEffect = { sideEffect ->
        when (sideEffect) {
            is HomeSideEffect.FetchHomeDataSideEffect -> {
                // TODO Decide if load user or not
                homeRepository.loadHomeScreen().fold(
                    onSuccess = {
                        HomeAction.HomeDataLoaded(
                            loadedSuccessfully = false,
                            userIdentity = null,
                            chosenLanguages = emptyList(),
                        )
                    },
                    onFailure = {
                        HomeAction.HomeDataLoaded(
                            loadedSuccessfully = false,
                            userIdentity = null,
                            chosenLanguages = emptyList(),
                        )
                    },
                )
            }

            is HomeSideEffect.FetchLearnUnitsSideEffect ->
                withContext(Dispatchers.IO) {
                    HomeAction.UnitsLoadSuccess(
                        contentRepository
                            .loadLearnUnits(30),
                    )
                }

            else -> null
        }
    },
)
