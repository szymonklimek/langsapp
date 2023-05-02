package com.klimek.langsapp.service.user.query.event

import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.query.storage.UserQueryRepository

class MessageBusEventsListener(
    messageBus: MessageBus,
    repository: UserQueryRepository
) : EventsListener(repository) {
    init {
        println("Registering listener $this to event bus: $messageBus")
        messageBus.register(UserCreatedEvent::class.java) {
            println("$this Received event: $it")
            if (it is UserCreatedEvent) onUserCreatedEvent(it)
        }
        messageBus.register(UserUpdatedEvent::class.java) {
            println("$this Received event: $it")
            if (it is UserUpdatedEvent) onUserUpdatedEvent(it)
        }
    }
}
