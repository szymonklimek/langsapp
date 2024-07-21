package com.langsapp.architecture

import com.langsapp.config.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class StateManager<
    State : com.langsapp.architecture.State,
    Action : com.langsapp.architecture.Action,
    >(
    // State to start with
    initialState: State,

    // Side effect emitted at the beginning
    val initialSideEffects: ArrayDeque<SideEffect>? = null,

    // Function handling the actions
    val handleAction: (State, Action) -> ActionResult<State>,

    // Optional handler of side effects that can result in actions to be passed to the state manager
    val handleSideEffect: (suspend (SideEffect) -> Action?)? = null,
) : ActionSender<Action> {
    var currentState = initialState
    var stateObserver: StateObserver<com.langsapp.architecture.State>? = null
        set(value) {
            Log.d("[$this] $value")
            field = value
            stateObserver?.onNewState(currentState, null, null)
        }
    var sideEffectConsumer: SideEffectConsumer<SideEffect>? = null
        set(value) {
            Log.d("[$this] $value")
            field = value
            Log.d("[$this] Initial side effects: $initialSideEffects")
            do {
                val sideEffect = initialSideEffects?.removeFirstOrNull()
                sideEffect?.let {
                    coroutineScope.launch {
                        handleSideEffect?.invoke(sideEffect)?.let { sendAction(it) }
                    }
                    Log.d("[$this] Notifying consumer: '$value' of side effect: '$sideEffect'")
                    value?.onSideEffect(it)
                }
            } while (sideEffect != null)
        }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun sendAction(action: Action) {
        Log.d("[$this] Action: '$action', current state: '$currentState'")
        val actionResult = handleAction(currentState, action)
        val previousState = currentState
        actionResult.newState?.let {
            Log.d("[$this] Updating state from: '$previousState' to '$it'")
            currentState = it
            stateObserver?.onNewState(currentState, previousState, actionResult.stateTransition)
        }

        actionResult.sideEffects.forEach { sideEffect ->
            coroutineScope.launch {
                handleSideEffect
                    ?.let {
                        Log.d("Executing side effect: $sideEffect")
                        val resultAction = it.invoke(sideEffect)
                        Log.d("SideEffect invocation resulted in action: $resultAction")
                        resultAction
                    }
                    ?.let {
                        sendAction(it)
                    }
            }
            sideEffectConsumer?.let {
                Log.d("[$this] Notifying consumer: '$sideEffectConsumer' of side effect: '$sideEffect'")
                sideEffectConsumer?.onSideEffect(sideEffect)
            } ?: Log.d("[$this] Ignoring side effect: '$sideEffect', consumer is missing")
        }
    }

    fun dispose() {
        stateObserver = null
        sideEffectConsumer = null
        coroutineScope.cancel()
    }
}
