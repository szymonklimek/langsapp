package com.klimek.langsapp.service.user.profile.event

import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.profile.UserProfileQueryService

abstract class EventsListener(
    private val service: UserProfileQueryService
) {
    fun onUserCreatedEvent(event: UserCreatedEvent) {
        service.handleUserCreatedEvent(event)
    }

    fun onUserUpdatedEvent(event: UserUpdatedEvent) {
        service.handleUserUpdatedEvent(event)
    }

    fun onUserFollowEvent(event: UserFollowEvent) {
        service.handleUserFollowEvent(event)
    }
}
