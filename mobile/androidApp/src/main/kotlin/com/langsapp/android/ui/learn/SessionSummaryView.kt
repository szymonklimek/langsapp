package com.langsapp.android.ui.learn

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.learn.LearnAction
import com.langsapp.learn.LearnState

@Composable
internal fun SessionSummaryView(
    actionSender: ActionSender<Action>,
    learnState: LearnState.SessionSummary,
) {
    Column {
        Text("SessionSummary")
        learnState.learnUnits.forEach {
            Text("Unit: $it")
        }

        Button(
            onClick = {
                actionSender.sendAction(LearnAction.FinishSessionTapped)
            },
        ) {
            Text("Finish session")
        }
    }
}
