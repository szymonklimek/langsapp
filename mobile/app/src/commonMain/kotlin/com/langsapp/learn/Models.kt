package com.langsapp.learn

data class Language(val name: String)

data class LanguageSettings(
    val learnLanguage: Language,
    val baseLanguage: Language,
    val supportLanguage: Language,
)

data class LearnUnit(
    val unitId: String,
    val learnLanguageText: String,
    val baseLanguageText: String,
    val supportLanguageText: String? = "",
)
