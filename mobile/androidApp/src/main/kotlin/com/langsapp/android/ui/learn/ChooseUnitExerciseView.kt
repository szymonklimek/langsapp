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
internal fun ChooseUnitExerciseView(
    actionSender: ActionSender<Action>,
    learnState: LearnState.ChooseUnitExercise,
) {
    Column {
        Text("ChooseUnitExercise: ${learnState.learnUnit}")
        Text("Answers: ${learnState.answers}")
        Button(
            onClick = {
                actionSender.sendAction(LearnAction.SubmitAnswer(learnState.learnUnit.learnLanguageText))
            },
        ) {
            Text("Submit correct answer")
        }

        Button(
            onClick = {
                actionSender.sendAction(LearnAction.StopSessionTapped)
            },
        ) {
            Text("Stop")
        }
    }
}
