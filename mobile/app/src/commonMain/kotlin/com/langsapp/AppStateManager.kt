package com.langsapp

import com.langsapp.architecture.Action
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.SideEffect
import com.langsapp.architecture.SideEffectConsumer
import com.langsapp.architecture.State
import com.langsapp.architecture.StateChange
import com.langsapp.architecture.StateManager
import com.langsapp.architecture.StateObserver
import com.langsapp.architecture.StateTransition
import com.langsapp.config.AppConfig
import com.langsapp.config.KeyValueStorage
import com.langsapp.config.Log
import com.langsapp.content.ManageContentRepository
import com.langsapp.content.ManageContentStateManager
import com.langsapp.data.ContentDatabase
import com.langsapp.data.ContentService
import com.langsapp.data.MockContentService
import com.langsapp.data.MockUserProfileService
import com.langsapp.data.UserProfileService
import com.langsapp.home.HomeNavigationSideEffect
import com.langsapp.home.HomeRepository
import com.langsapp.home.HomeStateManager
import com.langsapp.home.onboarding.OnBoardingRepository
import com.langsapp.home.onboarding.UserProfileRepository
import com.langsapp.identity.IdentityAction
import com.langsapp.identity.IdentityRepository
import com.langsapp.identity.IdentityStateManager
import com.langsapp.settings.language.LanguageSettingsRepository
import com.langsapp.settings.language.LanguageSettingsStateManager
import com.langsapp.userprofile.upsert.UpsertProfileStateManager

class AppStateManager(
    keyValueStorage: KeyValueStorage = AppConfig.keyValueStorage,
) : StateObserver<State>,
    SideEffectConsumer<SideEffect> {
    var currentState: State
    var stateObserver: StateObserver<State>? = null
    var sideEffectsConsumer: SideEffectConsumer<SideEffect>? = null

    private val contentService: ContentService = MockContentService()
    private val contentDatabase: ContentDatabase = ContentDatabase()
    private val manageContentRepository = ManageContentRepository(contentService, contentDatabase)
    private val userProfileService: UserProfileService = MockUserProfileService()
    private val userProfileRepository = UserProfileRepository(
        identityStateProvider = { identityStateManager.currentState },
        profileService = userProfileService,
        keyValueStorage = keyValueStorage,
    )

    private val identityStateManager = IdentityStateManager(
        anonymousUserId = AppConfig.uniqueInstallationId,
        tokenRefreshFunction = { Result.failure(NotImplementedError()) },
        identityRepository = IdentityRepository(keyValueStorage),
    )

    private val languageSettingsRepository = LanguageSettingsRepository(
        contentService = contentService,
        userProfileRepository = userProfileRepository,
    )

    private val homeStateManager = HomeStateManager(
        welcomeSlides = listOf(),
        authConfigProvider = { AppConfig.authConfig },
        homeRepository = HomeRepository(
            keyValueStorage = keyValueStorage,
            userProfileRepository = userProfileRepository,
            OnBoardingRepository(
                userProfileRepository = userProfileRepository,
                contentRepository = manageContentRepository,
            ),
        ),
    )

    private val stateManagersStack = ArrayDeque<StateManager<out State, out Action>>(listOf(homeStateManager))

    init {
        Log.d("$this")
        stateManagersStack.last().let {
            currentState = it.currentState
            it.stateObserver = this
            it.sideEffectConsumer = this
        }
        identityStateManager.stateObserver = object : StateObserver<State> {
            override fun onNewState(state: State, previousState: State?, transition: StateTransition?) {
                notifyStateManagersAboutExternalStateChange(StateChange(state, previousState, transition))
            }
        }
    }

    fun sendAction(action: Action) {
        when (action) {
            is IdentityAction -> identityStateManager.sendAction(action)
            else -> (stateManagersStack.last() as StateManager<*, in Action>).sendAction(action)
        }
    }

    override fun onSideEffect(sideEffect: SideEffect) {
        Log.d("$sideEffect")
        when (sideEffect) {
            is CommonSideEffect.NavigationSideEffect -> sideEffect.handle()
            is CommonSideEffect.ShowPopUpMessage -> sideEffectsConsumer?.onSideEffect(sideEffect)
        }
    }

    private fun CommonSideEffect.NavigationSideEffect.handle() {
        when (this) {
            is HomeNavigationSideEffect.SignUp -> sideEffectsConsumer?.onSideEffect(this)
            is HomeNavigationSideEffect.SelectLanguages -> stateManagersStack.addLast(
                LanguageSettingsStateManager(languageSettingsRepository)
                    .apply {
                        this.stateObserver = this@AppStateManager
                        this.sideEffectConsumer = this@AppStateManager
                    },
            )

            is HomeNavigationSideEffect.CreateProfile -> stateManagersStack.addLast(
                UpsertProfileStateManager(
                    currentProfile = null,
                    userProfileRepository = userProfileRepository,
                )
                    .apply {
                        this.stateObserver = this@AppStateManager
                        this.sideEffectConsumer = this@AppStateManager
                    },
            )

            is HomeNavigationSideEffect.ManageContent -> stateManagersStack.addLast(
                ManageContentStateManager(
                    languageSetting = languageSetting,
                    manageContentRepository = manageContentRepository,
                ).apply {
                    this.stateObserver = this@AppStateManager
                    this.sideEffectConsumer = this@AppStateManager
                },
            )

            is CommonSideEffect.Exit -> {
                stateManagersStack.removeLast().dispose()
                onNewState(
                    stateManagersStack.last().currentState,
                    currentState,
                    StateTransition(navigationType = StateTransition.Type.BACKWARD),
                )
            }
        }
    }

    override fun onNewState(state: State, previousState: State?, transition: StateTransition?) {
        Log.d("state: $state, transition: $transition")
        currentState = state
        stateObserver?.onNewState(state, previousState, transition)
        notifyStateManagersAboutExternalStateChange(StateChange(state, previousState, transition))
    }

    private fun notifyStateManagersAboutExternalStateChange(stateChange: StateChange<State>) {
        stateManagersStack.forEach {
            Log.d("Notifying $it about $stateChange")
            it.onExternalStateChange(stateChange)
        }
    }
}
