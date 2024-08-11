package com.langsapp.settings.language

import com.langsapp.architecture.State
import com.langsapp.model.Language

sealed class LanguageSettingsState : State {
    data object Loading : LanguageSettingsState()
    data object DataLoadingFailure : LanguageSettingsState()
    data class Input(
        val availableLanguages: List<Language>,
        val learnLanguage: Language?,
        val baseLanguage: Language?,
        val supportLanguage: Language?,
        val isInputCorrect: Boolean,
    ) : LanguageSettingsState()
}
