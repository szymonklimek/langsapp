package com.langsapp.android.ui.home

import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.home.HomeState

@Composable
fun HomeScreen(
    actionSender: ActionSender<Action>,
    homeState: HomeState,
) {
    when (homeState) {
        is HomeState.Welcome -> WelcomeScreen(actionSender, homeState)
        is HomeState.Loading -> LoadingScreen()
        is HomeState.Loaded -> LoadedHomeView(actionSender, homeState)
    }
}
