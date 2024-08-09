package com.langsapp.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.langsapp.AppStateManager
import com.langsapp.android.logging.Log
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.architecture.SideEffect
import com.langsapp.architecture.SideEffectConsumer
import com.langsapp.architecture.State
import com.langsapp.architecture.StateObserver
import com.langsapp.architecture.StateTransition
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel :
    ViewModel(),
    StateObserver<State>,
    ActionSender<Action>,
    SideEffectConsumer<SideEffect> {
    private val appStateManager = AppStateManager().apply {
        stateObserver = this@AppViewModel
        sideEffectsConsumer = this@AppViewModel
    }

    private val _uiState = MutableStateFlow<Pair<State, StateTransition?>>(appStateManager.currentState to null)
    private val _sideEffects = MutableSharedFlow<SideEffect>()

    val uiState = _uiState.asStateFlow()
    val sideEffects = _sideEffects.asSharedFlow()

    override fun onNewState(state: State, previousState: State?, transition: StateTransition?) {
        Log.d("$state")
        viewModelScope.launch {
            _uiState.emit(state to transition)
        }
    }

    override fun onSideEffect(sideEffect: SideEffect) {
        Log.d("$sideEffect")
        viewModelScope.launch {
            _sideEffects.emit(sideEffect)
        }
    }

    override fun sendAction(action: Action) {
        Log.d("$action")
        appStateManager.sendAction(action)
    }

    override fun onCleared() {
        super.onCleared()
        appStateManager.stateObserver = null
        appStateManager.sideEffectsConsumer = null
    }
}
