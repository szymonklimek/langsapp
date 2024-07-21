package com.langsapp.android.ui.learn

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.langsapp.architecture.Action
import com.langsapp.architecture.ActionSender
import com.langsapp.learn.LearnAction
import com.langsapp.learn.LearnState

@Composable
fun LearnScreen(
    actionSender: ActionSender<Action>,
    learnState: LearnState,
) {
    BackHandler(
        enabled = true,
        onBack = {
            actionSender.sendAction(LearnAction.StopSessionTapped)
        }
    )
    when (learnState) {
        is LearnState.UnitIntroduction -> UnitIntroductionView(actionSender, learnState)
        is LearnState.ChooseTranslationExercise -> ChooseTranslationExerciseView(actionSender, learnState)
        is LearnState.ChooseUnitExercise -> ChooseUnitExerciseView(actionSender, learnState)
        is LearnState.TranslationExercise -> TranslationExerciseView(actionSender, learnState)
        is LearnState.TranslationExerciseFailure -> TranslationExerciseFailureView(actionSender, learnState)
        is LearnState.TranslationExerciseSuccess -> TranslationExerciseSuccessView(actionSender, learnState)
        is LearnState.UnitSummary -> UnitSummaryView(actionSender, learnState)
        is LearnState.SessionSummary -> SessionSummaryView(actionSender, learnState)
    }
}
