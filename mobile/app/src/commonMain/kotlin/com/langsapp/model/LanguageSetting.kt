package com.langsapp.model

import kotlinx.serialization.Serializable

@Serializable
data class LanguageSetting(
    val learnLanguage: Language,
    val baseLanguage: Language,
    val supportLanguage: Language?,
)
