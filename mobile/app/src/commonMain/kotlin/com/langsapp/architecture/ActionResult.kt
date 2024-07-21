package com.langsapp.architecture

data class ActionResult<State : com.langsapp.architecture.State>(
    val newState: State? = null,
    val stateTransition: StateTransition? = null,
    val sideEffects: List<SideEffect> = emptyList(),
)
