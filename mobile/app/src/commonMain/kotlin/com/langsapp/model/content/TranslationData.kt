package com.langsapp.model.content

import com.langsapp.model.LanguageSetting

data class TranslationData(
    val languageSetting: LanguageSetting,
    val baseLanguageText: String,
    val learnLanguageText: String,
    val supportLanguageText: String?,
)
