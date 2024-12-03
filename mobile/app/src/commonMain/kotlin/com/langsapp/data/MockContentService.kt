package com.langsapp.data

import com.langsapp.model.Language
import com.langsapp.model.LanguageSetting
import com.langsapp.model.content.ContentUnit
import com.langsapp.model.content.TranslationData

class MockContentService : ContentService {

    override suspend fun fetchAvailableLanguages(): List<Language> =
        listOf(
            Language("en", "English"),
            Language("de", "German"),
            Language("pl", "Polish"),
            Language("ru", "Russian"),
            Language("es", "Spanish"),
            Language("fr", "French"),
            Language("uk", "Ukrainian")
        )

    override suspend fun getContentUnits(languageSetting: LanguageSetting): List<ContentUnit> =
        listOf(
            ContentUnit(
                id = "1",
                translationData = TranslationData(
                    languageSetting = languageSetting,
                    learnLanguageText = "one",
                    baseLanguageText = "jeden",
                    supportLanguageText = null,
                ),
            ),
            ContentUnit(
                id = "2",
                translationData = TranslationData(
                    languageSetting = languageSetting,
                    learnLanguageText = "two",
                    baseLanguageText = "dwa",
                    supportLanguageText = null,
                ),
            ),
        )
}
