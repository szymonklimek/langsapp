package com.klimek.langsapp.service.user.profile.storage

import com.klimek.langsapp.domain.LanguageSettings

data class UserProfileRecord(
    val id: String,
    val name: String? = null,
    val avatarUrl: String? = null,
    val languageSettings: LanguageSettings? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val lastComputedEventTimestamp: Long = System.currentTimeMillis(),
)
