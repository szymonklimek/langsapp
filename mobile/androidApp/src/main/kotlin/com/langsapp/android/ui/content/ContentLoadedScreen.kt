package com.langsapp.android.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.content.ManageContentAction
import com.langsapp.content.ManageContentState

@Composable
fun ContentLoadedScreen(
    actionSender: ActionSender<Action>,
    state: ManageContentState.Loaded,
) {
    Column {
        Text("Manage content screen loaded")
        Spacer(Modifier.height(32.dp))

        Text("Has content: ${state.hasContent}")

        Button(
            onClick = { actionSender.sendAction(ManageContentAction.DownloadUnitsTapped) },
        ) {
            Text("Download content")
        }
    }
}
