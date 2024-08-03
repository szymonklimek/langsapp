package com.klimek.langsapp.service.user.commands.event

import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.messagebus.MessageBus

class MessageBusUserEventsPublisher(private val messageBus: MessageBus) : UserEventsPublisher {
    override fun sendUserCreatedEvent(event: UserCreatedEvent) {
        messageBus.post(event)
    }

    override fun sendUserUpdatedEvent(event: UserUpdatedEvent) {
        messageBus.post(event)
    }
}
