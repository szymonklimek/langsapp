package com.langsapp.main

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class MainStateController(
    private val mainStateStorage: MainStateStorage
) {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    suspend fun init() {
        delay(2500)
        _uiState.update {
            val state =
            when {
                !mainStateStorage.wasWelcomeScreenAlreadyPassed() -> UiState.Welcome
                mainStateStorage.isIdentityRequired() -> UiState.Identity
                mainStateStorage.isOnBoardingRequired() -> UiState.OnBoarding
                else -> UiState.Home
            }
            println("State: $it")
            state
        }
    }
}
