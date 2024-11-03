package com.klimek.langsapp.events.user

import com.klimek.langsapp.domain.LanguageSettings
import com.klimek.langsapp.events.common.EventProperties

/**
 * Event representing the update of user fields
 *
 * @param eventProperties Default properties of the event
 * @param userId Unique user identifier
 * @param newUserName New name of the user
 * @param newAvatarUrl New avatar url to user's avatar
 * @param newLanguageSettings New user's language settings
 * @param propertiesToRemove Properties possible to remove
 */
data class UserUpdatedEvent(
    val eventProperties: EventProperties,
    val userId: String,
    val newUserName: String? = null,
    val newAvatarUrl: String? = null,
    val newLanguageSettings: LanguageSettings? = null,
    val propertiesToRemove: List<RemovableUserProperty>,
)

enum class RemovableUserProperty {
    AVATAR_URL,
}
