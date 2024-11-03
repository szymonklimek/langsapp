package com.klimek.langsapp.service.user.commands.storage

import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent

object UserCommandsInMemoryStorage {
    private val userCreatedEvents = mutableMapOf<String, UserCreatedEvent>()
    private val userUpdatedEvents = mutableMapOf<String, MutableList<UserUpdatedEvent>>()

    fun storeUserCreatedEvent(event: UserCreatedEvent) {
        userCreatedEvents[event.userId] = event
    }

    fun storeUserUpdatedEvent(event: UserUpdatedEvent) {
        val events = userUpdatedEvents[event.userId] ?: mutableListOf()
        events.add(event)
        userUpdatedEvents[event.userId] = events
    }
}
