package com.langsapp.main


sealed class UiState {
    data object Welcome : UiState()
    data object Loading : UiState()
    data object Identity : UiState()
    data object OnBoarding : UiState()
    data object Home : UiState()
    data object Learn: UiState()
    data object Repeat: UiState()
}
