package com.langsapp.learn

import com.langsapp.architecture.Action

sealed class LearnAction : Action {

    data object ContinueTapped : LearnAction()

    data class SubmitAnswer(val answer: String) : LearnAction()

    data object StopSessionTapped : LearnAction()

    data class SpeakerTapped(val learnUnit: LearnUnit) : LearnAction()

    data object AlreadyKnownTapped : LearnAction()

    data object MarkAsCorrectTapped : LearnAction()

    data class TranslationInputFieldChanged(val value: String) : LearnAction()

    data object FinishSessionTapped : LearnAction()
}
