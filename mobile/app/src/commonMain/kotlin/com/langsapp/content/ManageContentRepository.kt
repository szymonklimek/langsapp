package com.langsapp.content

import com.langsapp.config.Log
import com.langsapp.data.ContentDatabase
import com.langsapp.data.ContentService
import com.langsapp.model.LanguageSetting
import com.langsapp.model.content.ContentUnit

class ManageContentRepository(
    private val contentService: ContentService,
    private val contentDatabase: ContentDatabase,
) {

    suspend fun getAllUnits(languageSetting: LanguageSetting): Result<List<ContentUnit>> =
        runCatching {
            contentDatabase.getAllUnits(languageSetting)
        }
            .onFailure {
                Log.d("Failed to get content units: $it")
            }

    suspend fun downloadUnits(languageSetting: LanguageSetting): Result<Unit> =
        runCatching {
            contentService.getContentUnits(languageSetting)
        }
            .mapCatching {
                contentDatabase.insertUnits(languageSetting, it)
            }
}
