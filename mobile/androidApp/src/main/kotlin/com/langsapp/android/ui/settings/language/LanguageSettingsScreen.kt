package com.langsapp.android.ui.settings.language

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.settings.language.LanguageSettingsAction
import com.langsapp.settings.language.LanguageSettingsState

@Composable
internal fun LanguageSettingsScreen(
    actionSender: ActionSender<Action>,
    state: LanguageSettingsState,
) {
    BackHandler { actionSender.sendAction(LanguageSettingsAction.BackTapped) }
    when (state) {
        is LanguageSettingsState.Loading -> LoadingScreen()
        is LanguageSettingsState.Input -> InputScreen(actionSender, state)
        is LanguageSettingsState.DataLoadingFailure -> FailureScreen(actionSender, state)
    }
}
