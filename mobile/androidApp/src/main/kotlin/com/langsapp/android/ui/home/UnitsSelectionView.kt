package com.langsapp.android.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.home.HomeAction
import com.langsapp.home.HomeState

@Composable
internal fun UnitsSelectionView(
    actionSender: ActionSender<Action>,
    state: HomeState.UnitsSelection,
) {
    Column {
        Text("UnitsSelectionView")
        Button(
            onClick = {
                actionSender.sendAction(HomeAction.UnitsNumberSubmitted(count = 10))
            },
        ) {
            Text("Select 10 units")
        }
    }
}
