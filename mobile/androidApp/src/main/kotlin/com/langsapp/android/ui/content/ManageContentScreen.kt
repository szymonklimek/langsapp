package com.langsapp.android.ui.content

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.content.ManageContentAction
import com.langsapp.content.ManageContentState

@Composable
fun ManageContentScreen(
    actionSender: ActionSender<Action>,
    state: ManageContentState,
) {
    BackHandler { actionSender.sendAction(ManageContentAction.BackTapped) }
    when (state) {
        is ManageContentState.Loading -> LoadingScreen()
        is ManageContentState.Loaded -> ContentLoadedScreen(actionSender, state)
        is ManageContentState.Failure -> ContentLoadFailureScreen(actionSender, state)
    }
}
