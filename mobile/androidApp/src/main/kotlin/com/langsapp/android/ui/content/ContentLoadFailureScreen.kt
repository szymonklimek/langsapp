package com.langsapp.android.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.content.ManageContentAction
import com.langsapp.content.ManageContentState

@Composable
fun ContentLoadFailureScreen(
    actionSender: ActionSender<Action>,
    state: ManageContentState.Failure,
) {
    Column {
        Text("Manage content failure screen")
        Button(
            onClick = { actionSender.sendAction(ManageContentAction.DownloadUnitsTapped) },
        ) {
            Text("Retry download content")
        }
    }
}
