package com.langsapp.settings.language

import com.langsapp.data.ContentService
import com.langsapp.home.onboarding.UserProfileRepository
import com.langsapp.model.Language
import com.langsapp.model.LanguageSetting

class LanguageSettingsRepository(
    private val contentService: ContentService,
    private val userProfileRepository: UserProfileRepository,
) {
    suspend fun fetchAvailableLanguages(): Result<List<Language>> =
        runCatching { contentService.fetchAvailableLanguages() }

    suspend fun saveSelectedLanguages(languageSetting: LanguageSetting): Result<Unit> =
        userProfileRepository.updateLanguageSetting(languageSetting)
}
