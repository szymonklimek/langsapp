package com.langsapp.architecture

interface StateObserver<State : com.langsapp.architecture.State> {
    fun onNewState(
        state: State,
        previousState: State?,
        transition: StateTransition?,
    )
}
