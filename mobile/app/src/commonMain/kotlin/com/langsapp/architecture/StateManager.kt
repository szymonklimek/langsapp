package com.langsapp.architecture

import com.langsapp.config.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Class managing the [State] based on received [Action] notifying [stateObserver] everytime [currentState] changes
 * and [sideEffectConsumer] each time [SideEffect] occurred.
 *
 * @param initialState [State] to begin with
 * @param initialSideEffects List of [SideEffect] emitted at the creation of the class
 * @param handleAction Function that handles [Action]
 * @param handleSideEffect Optional function that can turn [SideEffect] into [Action]
 * @param onExternalStateCoordination Optional function that turn external [StateChange] into [Action]
 * and be passed back to [StateManager]
 */
abstract class StateManager<
    State : com.langsapp.architecture.State,
    Action : com.langsapp.architecture.Action,
    >(
    initialState: State,
    val initialSideEffects: ArrayDeque<SideEffect>? = null,
    val handleAction: (State, Action) -> ActionResult<State>,
    val handleSideEffect: (suspend (SideEffect) -> Action?)? = null,
    val onExternalStateCoordination: ((StateChange<com.langsapp.architecture.State>) -> Action?)? = null,
) : ActionSender<Action> {
    var currentState = initialState
    var stateObserver: StateObserver<com.langsapp.architecture.State>? = null
        set(value) {
            Log.d("[$this] $value")
            field = value
            stateObserver?.onNewState(
                currentState,
                null,
                StateTransition(navigationType = StateTransition.Type.FORWARD),
            )
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

    fun onExternalStateChange(stateChange: StateChange<com.langsapp.architecture.State>) {
        onExternalStateCoordination
            ?.invoke(stateChange)
            ?.let { action ->
                Log.d("[$this] Sending action: '$action' from external state: '$stateChange'")
                sendAction(action)
            }
    }

    fun dispose() {
        stateObserver = null
        sideEffectConsumer = null
        coroutineScope.cancel()
    }
}
