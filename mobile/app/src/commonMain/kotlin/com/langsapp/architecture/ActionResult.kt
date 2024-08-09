package com.langsapp.architecture

/**
 * Class grouping all possible results sending [Action] to [StateManager]
 *
 * @param newState New [State] that the action results in
 * @param stateTransition [StateTransition] describing transition process into [newState]
 * @param sideEffects List of [SideEffect] that the action results in
 */
data class ActionResult<State : com.langsapp.architecture.State>(
    val newState: State? = null,
    val stateTransition: StateTransition? = null,
    val sideEffects: List<SideEffect> = emptyList(),
)
