package com.langsapp.learn

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.StateManager
import com.langsapp.text2speech.SpeakTextSideEffect

class LearnStateManager(
    units: List<LearnUnit>,
) : StateManager<LearnState, LearnAction>(
    initialState = LearnState.UnitIntroduction(units.first()),
    initialSideEffects = ArrayDeque(listOf(SpeakTextSideEffect(units.first().learnLanguageText))),
    handleAction = { currentState, learnAction ->
        when (learnAction) {
            LearnAction.ContinueTapped -> LearnAction.ContinueTapped.handle(units, currentState)
            is LearnAction.SpeakerTapped -> learnAction.handle(currentState)
            is LearnAction.SubmitAnswer -> learnAction.handle(currentState)
            is LearnAction.TranslationInputFieldChanged -> learnAction.handle(currentState)
            is LearnAction.StopSessionTapped -> learnAction.handle(currentState)
            LearnAction.MarkAsCorrectTapped -> LearnAction.MarkAsCorrectTapped.handle(currentState)
            LearnAction.AlreadyKnownTapped -> LearnAction.AlreadyKnownTapped.handle(currentState)
            LearnAction.FinishSessionTapped -> LearnAction.FinishSessionTapped.handle(currentState)
        }
    },
)

private fun LearnAction.ContinueTapped.handle(units: List<LearnUnit>, currentState: LearnState) =
    when (currentState) {
        is LearnState.UnitIntroduction -> {
            val currentUnitIndex = units.indexOf(currentState.learnUnit)
            val newUnit = if (currentUnitIndex == units.lastIndex) {
                units.first()
            } else {
                units[currentUnitIndex + 1]
            }
            ActionResult(
                newState =
                if (currentUnitIndex == units.lastIndex) {
                    LearnState.ChooseUnitExercise(
                        learnUnit = newUnit,
                        // TODO How to load and provide answers !!
                        answers = listOf("", ""),
                    )
                } else {
                    LearnState.UnitIntroduction(newUnit)
                },
                // TODO Add potential state change animation here
                sideEffects = listOf(
                    SpeakTextSideEffect(newUnit.learnLanguageText),
                ),
            )
        }

        is LearnState.ChooseUnitExercise -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseTranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseSuccess -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseFailure -> ActionResult<LearnState>(currentState)
        is LearnState.UnitSummary -> ActionResult<LearnState>(currentState)
        is LearnState.SessionSummary -> ActionResult<LearnState>(currentState)
    }

private fun LearnAction.StopSessionTapped.handle(currentState: LearnState) =
    when (currentState) {
        is LearnState.UnitIntroduction -> ActionResult(sideEffects = listOf(LearnNavigationSideEffect.Exit))
        is LearnState.ChooseUnitExercise -> ActionResult(sideEffects = listOf(LearnNavigationSideEffect.Exit))
        is LearnState.ChooseTranslationExercise -> ActionResult(sideEffects = listOf(LearnNavigationSideEffect.Exit))
        is LearnState.TranslationExercise -> ActionResult(sideEffects = listOf(LearnNavigationSideEffect.Exit))
        is LearnState.TranslationExerciseSuccess -> ActionResult(sideEffects = listOf(LearnNavigationSideEffect.Exit))
        is LearnState.TranslationExerciseFailure -> ActionResult(sideEffects = listOf(LearnNavigationSideEffect.Exit))
        is LearnState.UnitSummary -> ActionResult(sideEffects = listOf(LearnNavigationSideEffect.Exit))
        is LearnState.SessionSummary -> ActionResult<LearnState>(sideEffects = listOf(LearnNavigationSideEffect.Exit))
    }

private fun LearnAction.AlreadyKnownTapped.handle(currentState: LearnState) =
    when (currentState) {
        is LearnState.UnitIntroduction -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseUnitExercise -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseTranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseSuccess -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseFailure -> ActionResult<LearnState>(currentState)
        is LearnState.UnitSummary -> ActionResult<LearnState>(currentState)
        is LearnState.SessionSummary -> ActionResult<LearnState>(currentState)
    }

private fun LearnAction.FinishSessionTapped.handle(currentState: LearnState) =
    when (currentState) {
        is LearnState.UnitIntroduction -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseUnitExercise -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseTranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseSuccess -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseFailure -> ActionResult<LearnState>(currentState)
        is LearnState.UnitSummary -> ActionResult<LearnState>(currentState)
        is LearnState.SessionSummary -> ActionResult<LearnState>(currentState)
    }

private fun LearnAction.SpeakerTapped.handle(currentState: LearnState) =
    when (currentState) {
        is LearnState.UnitIntroduction -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseUnitExercise -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseTranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseSuccess -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseFailure -> ActionResult<LearnState>(currentState)
        is LearnState.UnitSummary -> ActionResult<LearnState>(currentState)
        is LearnState.SessionSummary -> ActionResult<LearnState>(currentState)
    }

private fun LearnAction.SubmitAnswer.handle(currentState: LearnState) =
    when (currentState) {
        is LearnState.UnitIntroduction -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseUnitExercise -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseTranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseSuccess -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseFailure -> ActionResult<LearnState>(currentState)
        is LearnState.UnitSummary -> ActionResult<LearnState>(currentState)
        is LearnState.SessionSummary -> ActionResult<LearnState>(currentState)
    }

private fun LearnAction.TranslationInputFieldChanged.handle(currentState: LearnState) =
    when (currentState) {
        is LearnState.UnitIntroduction -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseUnitExercise -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseTranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseSuccess -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseFailure -> ActionResult<LearnState>(currentState)
        is LearnState.UnitSummary -> ActionResult<LearnState>(currentState)
        is LearnState.SessionSummary -> ActionResult<LearnState>(currentState)
    }

private fun LearnAction.MarkAsCorrectTapped.handle(currentState: LearnState) =
    when (currentState) {
        is LearnState.UnitIntroduction -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseUnitExercise -> ActionResult<LearnState>(currentState)
        is LearnState.ChooseTranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExercise -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseSuccess -> ActionResult<LearnState>(currentState)
        is LearnState.TranslationExerciseFailure -> ActionResult<LearnState>(currentState)
        is LearnState.UnitSummary -> ActionResult<LearnState>(currentState)
        is LearnState.SessionSummary -> ActionResult<LearnState>(currentState)
    }
