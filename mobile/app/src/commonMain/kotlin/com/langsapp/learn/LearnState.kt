package com.langsapp.learn

import com.langsapp.architecture.State

sealed class LearnState : State {

    data class UnitIntroduction(
        val learnUnit: LearnUnit,
    ) : LearnState()

    data class ChooseTranslationExercise(
        val learnUnit: LearnUnit,
        val answers: List<String>,
    ) : LearnState()

    data class ChooseUnitExercise(
        val learnUnit: LearnUnit,
        val answers: List<String>,
    ) : LearnState()

    data class TranslationExercise(
        val learnUnit: LearnUnit,
    ) : LearnState()

    data class TranslationExerciseSuccess(
        val learnUnit: LearnUnit,
    ) : LearnState()

    data class TranslationExerciseFailure(
        val enteredValue: String,
        val learnUnit: LearnUnit,
    ) : LearnState()

    data class UnitSummary(
        val learnUnit: LearnUnit,
    ) : LearnState()

    data class SessionSummary(
        val learnUnits: List<LearnUnit>,
    ) : LearnState()
}
