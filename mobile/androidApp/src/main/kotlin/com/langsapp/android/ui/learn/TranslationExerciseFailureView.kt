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
internal fun TranslationExerciseFailureView(
    actionSender: ActionSender<Action>,
    learnState: LearnState.TranslationExerciseFailure,
) {
    Column {
        Text("TranslationExerciseFailure: ${learnState.learnUnit}")
        Text("Entered value: ${learnState.enteredValue}")

        Button(
            onClick = {
                actionSender.sendAction(LearnAction.ContinueTapped)
            },
        ) {
            Text("Continue")
        }

        Button(
            onClick = {
                actionSender.sendAction(LearnAction.MarkAsCorrectTapped)
            },
        ) {
            Text("Mark as correct")
        }
    }
}
