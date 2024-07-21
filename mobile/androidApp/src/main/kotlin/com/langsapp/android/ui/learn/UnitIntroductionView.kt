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
internal fun UnitIntroductionView(
    actionSender: ActionSender<Action>,
    learnState: LearnState.UnitIntroduction,
) {
    Column {
        Text("UnitIntroductionView: ${learnState.learnUnit}")
        Button(
            onClick = {
                actionSender.sendAction(LearnAction.SpeakerTapped(learnState.learnUnit))
            },
        ) {
            Text("Speaker")
        }

        Button(
            onClick = {
                actionSender.sendAction(LearnAction.AlreadyKnownTapped)
            },
        ) {
            Text("Already known")
        }

        Button(
            onClick = {
                actionSender.sendAction(LearnAction.ContinueTapped)
            },
        ) {
            Text("Continue")
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
