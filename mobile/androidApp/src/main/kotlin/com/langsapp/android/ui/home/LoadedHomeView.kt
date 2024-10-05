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
internal fun LoadedHomeView(
    actionSender: ActionSender<Action>,
    state: HomeState.Loaded,
) {
    Column {
        Text("Home loaded")
        Button(onClick = {
            actionSender.sendAction(HomeAction.SignUpTapped)
        }) {
            Text("Sign up")
        }
    }
}
