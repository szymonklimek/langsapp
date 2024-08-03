package com.klimek.langsapp.events.follow

import com.klimek.langsapp.events.common.EventProperties

/**
 * Event representing the change of state in following relation between users
 *
 * @param eventProperties Default properties of the event
 * @param followerUserId Identifier of user that intents to follow or stop following [userId]
 * @param userId User identifier that is followed or not followed depending on [isFollowed] flag
 * @param isFollowed Flag indicating if [userId] is followed or stop being followed by [followerUserId]
 */
data class UserFollowEvent(
    val eventProperties: EventProperties,
    val followerUserId: String,
    val userId: String,
    val isFollowed: Boolean,
)
