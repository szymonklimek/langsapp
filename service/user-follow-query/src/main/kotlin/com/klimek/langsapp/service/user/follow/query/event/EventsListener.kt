package com.klimek.langsapp.service.user.follow.query.event

import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.service.user.follow.query.FollowerUserId
import com.klimek.langsapp.service.user.follow.query.UserFollow
import com.klimek.langsapp.service.user.follow.query.UserId
import com.klimek.langsapp.service.user.follow.query.storage.UserFollowQueryRepository

abstract class EventsListener(
    private val repository: UserFollowQueryRepository,
) {
    fun onUserFollowEvent(event: UserFollowEvent) {
        repository.createOrUpdateUserFollow(
            UserFollow(
                userId = UserId(event.userId),
                followerUserId = FollowerUserId(event.followerUserId),
                isFollowed = event.isFollowed,
            ),
        )
    }
}
