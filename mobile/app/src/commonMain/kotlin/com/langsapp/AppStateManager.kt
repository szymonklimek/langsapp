package com.langsapp

import com.langsapp.architecture.Action
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.SideEffect
import com.langsapp.architecture.SideEffectConsumer
import com.langsapp.architecture.State
import com.langsapp.architecture.StateManager
import com.langsapp.architecture.StateObserver
import com.langsapp.architecture.StateTransition
import com.langsapp.config.AppConfig
import com.langsapp.config.KeyValueStorage
import com.langsapp.config.Log
import com.langsapp.home.HomeNavigationSideEffect
import com.langsapp.home.HomeRepository
import com.langsapp.home.HomeStateManager
import com.langsapp.home.welcome.WelcomeSlide
import com.langsapp.identity.IdentityAction
import com.langsapp.identity.IdentityRepository
import com.langsapp.identity.IdentityStateManager
import com.langsapp.learn.LearnNavigationSideEffect
import com.langsapp.learn.LearnStateManager
import com.langsapp.text2speech.SpeakTextSideEffect

class AppStateManager(keyValueStorage: KeyValueStorage = AppConfig.keyValueStorage) :
    StateObserver<State>,
    SideEffectConsumer<SideEffect> {
    var currentState: State
    var stateObserver: StateObserver<State>? = null
    var sideEffectsConsumer: SideEffectConsumer<SideEffect>? = null

    // region Identity integration

    private val identityStateManager = IdentityStateManager(
        anonymousUserId = AppConfig.uniqueInstallationId,
        identityRepository = IdentityRepository(keyValueStorage),
    ).apply {
        // TODO Figure out identity integration and how it can affect other states
//        this.stateObserver = object : StateObserver<State> {
//            override fun onNewState(state: State) {
//                if (state is IdentityState) {
//                    stateManagersStack.forEach {
//                        // Inform all state managers about changes in identity
//                    }
//                }
//            }
//        }
    }

    // endregion

    private val appStateRepository = AppStateRepository(keyValueStorage)
    private val stateManagersStack = ArrayDeque<StateManager<out State, out Action>>(
        listOf(
            HomeStateManager(
                shouldShowWelcome = { !appStateRepository.hasShownWelcome() },
                welcomeSlides = listOf(
                    WelcomeSlide("First slide"),
                    WelcomeSlide("Second slide"),
                    WelcomeSlide("Third slide"),
                ),
                contentRepository = ContentRepository(),
                homeRepository = HomeRepository(),
            ).apply {
                stateObserver = this@AppStateManager
                sideEffectConsumer = this@AppStateManager
            },
        ),
    )

    init {
        Log.d("$this")
        currentState = stateManagersStack.last().currentState
    }

    fun sendAction(action: Action) {
        when (action) {
            is IdentityAction -> identityStateManager.sendAction(action)
            else -> (currentStateManager() as StateManager<*, in Action>).sendAction(action)
        }
    }

    override fun onSideEffect(sideEffect: SideEffect) {
        Log.d("$sideEffect")
        when (sideEffect) {
            is CommonSideEffect.NavigationSideEffect -> handleNavigationSideEffect(sideEffect)
            is CommonSideEffect.ShowPopUpMessage -> sideEffectsConsumer?.onSideEffect(sideEffect)
            is SpeakTextSideEffect -> sideEffectsConsumer?.onSideEffect(sideEffect)
        }
    }

    private fun handleNavigationSideEffect(sideEffect: CommonSideEffect.NavigationSideEffect) {
        when (sideEffect) {
            is HomeNavigationSideEffect.SkipWelcome -> Unit
            is HomeNavigationSideEffect.NavigateToLogin -> sideEffectsConsumer?.onSideEffect(sideEffect)
            is HomeNavigationSideEffect.Learn ->
                // TODO How to implement screen transition animation
                stateManagersStack.add(
                    LearnStateManager(units = sideEffect.unitsToLearn).apply {
                        this.stateObserver = this@AppStateManager
                        this.sideEffectConsumer = this@AppStateManager
                    },
                )

            is LearnNavigationSideEffect.Exit -> {
                // TODO How to implement screen transition animation
                stateManagersStack.removeLast().dispose()
                onNewState(stateManagersStack.last().currentState, null, object : StateTransition {})
            }
        }
    }

    override fun onNewState(state: State, previousState: State?, transition: StateTransition?) {
        Log.d("state: $state, transition: $transition")
        stateObserver?.onNewState(state, previousState, transition)
    }

    private fun currentStateManager() = stateManagersStack.last()
}
