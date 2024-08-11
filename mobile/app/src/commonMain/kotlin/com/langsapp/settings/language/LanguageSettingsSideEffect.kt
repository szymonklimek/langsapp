package com.langsapp.settings.language

import com.langsapp.architecture.SideEffect
import com.langsapp.model.Language

sealed class LanguageSettingsSideEffect : SideEffect {
    data object FetchAvailableLanguages : LanguageSettingsSideEffect()
    data class ConfirmLanguagesSelection(
        val availableLanguages: List<Language>,
        val learnLanguage: Language,
        val baseLanguage: Language,
        val supportLanguage: Language? = null,
    ) : LanguageSettingsSideEffect()
}
