package com.langsapp.android.ui.userprofile.upsert

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.userprofile.upsert.UpsertProfileAction
import com.langsapp.userprofile.upsert.UpsertProfileState

@Composable
internal fun UpsertProfileScreen(
    actionSender: ActionSender<Action>,
    state: UpsertProfileState,
) {
    BackHandler { actionSender.sendAction(UpsertProfileAction.BackTapped) }
    when (state) {
        is UpsertProfileState.Loading -> LoadingScreen()
        is UpsertProfileState.Input -> InputScreen(actionSender, state)
    }
}
