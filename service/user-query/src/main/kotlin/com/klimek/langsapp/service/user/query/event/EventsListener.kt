package com.klimek.langsapp.service.user.query.event

import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.user.query.UserId
import com.klimek.langsapp.service.user.query.UserName
import com.klimek.langsapp.service.user.query.storage.UserQueryRepository

abstract class EventsListener(
    private val repository: UserQueryRepository,
) {
    fun onUserCreatedEvent(event: UserCreatedEvent) {
        repository
            .storeUser(
                UserId(event.userId),
                UserName(event.userName),
            )
    }

    fun onUserUpdatedEvent(event: UserUpdatedEvent) {
        repository
            .storeUser(
                UserId(event.userId),
                UserName(event.userName),
            )
    }
}
