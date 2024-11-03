package com.klimek.langsapp.domain

data class LanguageSettings(
    val learnLanguage: Language,
    val baseLanguage: Language,
    val supportLanguage: Language?,
)
