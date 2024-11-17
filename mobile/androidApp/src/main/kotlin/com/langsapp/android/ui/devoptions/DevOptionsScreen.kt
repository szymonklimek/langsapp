package com.langsapp.android.ui.devoptions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.devoptions.DevOptionsAction
import com.langsapp.devoptions.DevOptionsState

@Composable
fun DevOptionsScreen(
    actionSender: ActionSender<Action>,
    state: DevOptionsState,
) {
    BackHandler { actionSender.sendAction(DevOptionsAction.BackTapped) }
    when (state) {
        is DevOptionsState.Loading -> CircularProgressIndicator()
        is DevOptionsState.Loaded ->
            Column {
                Text("Dev Options")
                val selectedOption = remember { mutableStateOf(state.selectedEnvironment.name) }
                state.apiEnvironments.forEach {
                    Row {
                        RadioButton(
                            selected = selectedOption.value == it.name,
                            onClick = {
                                selectedOption.value = it.name
                                actionSender.sendAction(DevOptionsAction.EnvironmentChanged(it))
                            },
                        )
                        Text("${it.name}: ${it.apiUrl}")
                    }
                }
                Spacer(Modifier.height(32.dp))
                Text("Selected env: ${state.selectedEnvironment}")
            }
    }
}
