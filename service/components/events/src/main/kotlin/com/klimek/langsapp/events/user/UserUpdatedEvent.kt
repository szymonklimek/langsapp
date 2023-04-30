package com.klimek.langsapp.events.user

import com.klimek.langsapp.events.common.EventProperties

/**
 * Event representing the update of user fields
 *
 * @param eventProperties Default properties of the event
 * @param userId Unique user identifier
 * @param userName Name of the user
 * @param avatarUrl Url to user's avatar
 */
data class UserUpdatedEvent(
    val eventProperties: EventProperties,
    val userId: String,
    val userName: String,
    val avatarUrl: String? = null
)
