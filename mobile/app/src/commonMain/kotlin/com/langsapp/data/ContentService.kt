package com.langsapp.data

import com.langsapp.model.Language
import com.langsapp.model.LanguageSetting
import com.langsapp.model.content.ContentUnit

interface ContentService {

    suspend fun fetchAvailableLanguages(): List<Language>

    suspend fun getContentUnits(languageSetting: LanguageSetting): List<ContentUnit>
}
