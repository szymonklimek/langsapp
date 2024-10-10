package com.langsapp.android.ui.userprofile.upsert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.langsapp.android.logging.Log
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.userprofile.upsert.UpsertProfileAction
import com.langsapp.userprofile.upsert.UpsertProfileState

@Composable
internal fun InputScreen(
    actionSender: ActionSender<Action>,
    state: UpsertProfileState.Input,
) {
    Column {
        Text("Upsert profile")
        Spacer(Modifier.height(16.dp))

        Text("Current profile: ${state.currentProfile}")
        Spacer(Modifier.height(16.dp))

        var username by remember { mutableStateOf(TextFieldValue(state.currentProfile?.username ?: "")) }
        TextField(
            value = username,
            onValueChange = {
                username = it
            },
            label = { Text(text = "Username") },
            placeholder = { Text(text = "Type username here") },
        )

        Button(
            onClick = {
                Log.d("Confirm tapped")
                actionSender.sendAction(UpsertProfileAction.ConfirmTapped(username.text))
            },
            enabled = true,
        ) {
            Text("Confirm")
        }
    }
}
