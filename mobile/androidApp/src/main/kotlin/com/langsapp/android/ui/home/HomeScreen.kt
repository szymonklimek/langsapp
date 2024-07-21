package com.langsapp.android.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.home.HomeAction
import com.langsapp.home.HomeState

@Composable
fun HomeScreen(
    actionSender: ActionSender<Action>,
    homeState: HomeState,
) {

    BackHandler(
        enabled = true,
        onBack = { actionSender.sendAction(HomeAction.BackTapped) }
    )
    when (homeState) {
        is HomeState.Welcome -> WelcomeScreen(actionSender, homeState)
        is HomeState.Loading -> LoadingScreen()
        is HomeState.Loaded -> LoadedHomeView(actionSender, homeState)
        is HomeState.UnitsSelection -> UnitsSelectionView(actionSender, homeState)
    }
}
