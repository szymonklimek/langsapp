package com.klimek.langsapp.service.user.query.event

import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.query.UserQueryService

abstract class EventsListener(
    private val userQueryService: UserQueryService,
) {
    fun onUserCreatedEvent(event: UserCreatedEvent) {
        userQueryService.handleUserCreatedEvent(event)
    }

    fun onUserUpdatedEvent(event: UserUpdatedEvent) {
        userQueryService.handleUserUpdatedEvent(event)
    }
}
