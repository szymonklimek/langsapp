package com.langsapp.android.ui.settings.language

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.langsapp.android.logging.Log
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.settings.language.LanguageSettingsAction
import com.langsapp.settings.language.LanguageSettingsState

@Composable
internal fun FailureScreen(
    actionSender: ActionSender<Action>,
    state: LanguageSettingsState.DataLoadingFailure,
) {
    Column {
        Text("Language settings failure")
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                Log.d("Retry tapped")
                actionSender.sendAction(LanguageSettingsAction.RetryTapped)
            },
        ) {
            Text("Retry")
        }
    }
}
