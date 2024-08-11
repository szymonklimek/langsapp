package com.langsapp.settings.language

import com.langsapp.architecture.Action
import com.langsapp.model.Language

sealed class LanguageSettingsAction : Action {
    data class AvailableLanguagesLoaded(val languages: List<Language>) : LanguageSettingsAction()
    data object AvailableLanguagesLoadFailure : LanguageSettingsAction()
    data class LearnLanguageChanged(val language: Language) : LanguageSettingsAction()
    data class BaseLanguageChanged(val language: Language) : LanguageSettingsAction()
    data class SupportLanguageChanged(val language: Language?) : LanguageSettingsAction()
    data object ConfirmTapped : LanguageSettingsAction()
    data object SelectionSavingSucceeded : LanguageSettingsAction()
    data class SelectionSavingFailed(
        val availableLanguages: List<Language>,
        val learnLanguage: Language,
        val baseLanguage: Language,
        val supportLanguage: Language?,
    ) : LanguageSettingsAction()
    data object BackTapped : LanguageSettingsAction()
    data object RetryTapped : LanguageSettingsAction()
}
