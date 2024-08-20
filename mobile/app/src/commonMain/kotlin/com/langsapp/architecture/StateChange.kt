package com.langsapp.architecture

data class StateChange<State>(
    val newState: State,
    val previousState: State? = null,
    val transition: StateTransition? = null,
)
