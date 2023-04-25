package com.klimek.langsapp.service.user.commands.storage

import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent

object UserCommandsInMemoryStorage {
    val userNamesToIds = mutableMapOf<String, String>()
    val userIdsToNames = mutableMapOf<String, String>()
    private val userCreatedEvents = mutableMapOf<String, UserCreatedEvent>()
    private val userUpdatedEvents = mutableMapOf<String, MutableList<UserUpdatedEvent>>()

    fun storeUserCreatedEvent(event: UserCreatedEvent) {
        userNamesToIds[event.userName] = event.userId
        userIdsToNames[event.userId] = event.userName
        userCreatedEvents[event.userId] = event
    }

    fun storeUserUpdatedEvent(event: UserUpdatedEvent) {
        val events = userUpdatedEvents[event.userId] ?: mutableListOf()
        events.add(event)
        event.userName?.let {
            userNamesToIds[event.userId] = it
            userIdsToNames[event.userId]
        }
        userUpdatedEvents[event.userId] = events
    }
}
