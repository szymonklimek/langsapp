package com.langsapp.android.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.home.HomeAction
import com.langsapp.home.HomeState

@Composable
fun WelcomeScreen(
    actionSender: ActionSender<Action>,
    welcomeState: HomeState.Welcome,
) {
    Column {
        Text("Slides: ${welcomeState.slides}")
        Button(
            onClick = {
                actionSender.sendAction(HomeAction.SkipTapped)
            },
        ) {
            Text("Skip")
        }
        if (welcomeState.devOptionsEnabled) {
            Button(
                onClick = {
                    actionSender.sendAction(HomeAction.DevOptionsTapped)
                },
            ) {
                Text("Dev options")
            }
        }
    }
}
