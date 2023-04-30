package com.klimek.langsapp.service.user.follow.commands.storage

import com.klimek.langsapp.events.follow.UserFollowEvent

object UserFollowCommandsInMemoryStorage {

    data class StorageKey(val followerUserId: String, val userId: String)

    private val lastUserFollowEvents = mutableMapOf<StorageKey, UserFollowEvent>()

    fun getLastEventForUser(followerUserId: String, userId: String) =
        lastUserFollowEvents[StorageKey(followerUserId, userId)]

    fun storeUserFollowEvent(event: UserFollowEvent) {
        lastUserFollowEvents[StorageKey(event.followerUserId, event.userId)] = event
    }
}
