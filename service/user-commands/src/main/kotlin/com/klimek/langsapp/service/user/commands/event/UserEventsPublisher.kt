package com.klimek.langsapp.service.user.commands.event

import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent


interface UserEventsPublisher {
    fun sendUserCreatedEvent(event: UserCreatedEvent)
    fun sendUserUpdatedEvent(event: UserUpdatedEvent)
}
