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
import com.langsapp.home.HomeRepository
import com.langsapp.home.HomeStateManager

class AppStateManager(keyValueStorage: KeyValueStorage = AppConfig.keyValueStorage) :
    StateObserver<State>,
    SideEffectConsumer<SideEffect> {
    var currentState: State
    var stateObserver: StateObserver<State>? = null
    var sideEffectsConsumer: SideEffectConsumer<SideEffect>? = null

    private val homeStateManager = HomeStateManager(
        welcomeSlides = listOf(),
        homeRepository = HomeRepository(keyValueStorage),
    )

    private val stateManagersStack = ArrayDeque<StateManager<out State, out Action>>(listOf(homeStateManager))

    init {
        Log.d("$this")
        stateManagersStack.last().let {
            currentState = it.currentState
            it.stateObserver = this
            it.sideEffectConsumer = this
        }
    }

    fun sendAction(action: Action) {
        (currentStateManager() as StateManager<*, in Action>).sendAction(action)
    }

    override fun onSideEffect(sideEffect: SideEffect) {
        Log.d("$sideEffect")
        when (sideEffect) {
            is CommonSideEffect.NavigationSideEffect -> Unit
            is CommonSideEffect.ShowPopUpMessage -> sideEffectsConsumer?.onSideEffect(sideEffect)
        }
    }

    override fun onNewState(state: State, previousState: State?, transition: StateTransition?) {
        Log.d("state: $state, transition: $transition")
        currentState = state
        stateObserver?.onNewState(state, previousState, transition)
    }

    private fun currentStateManager() = stateManagersStack.last()
}
