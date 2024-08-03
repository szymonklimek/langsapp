package com.klimek.langsapp.service.user.profile.event

import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.events.user.UserCreatedEvent
import com.klimek.langsapp.events.user.UserUpdatedEvent
import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.profile.UserProfileQueryService

class MessageBusEventsListener(
    messageBus: MessageBus,
    service: UserProfileQueryService,
) : EventsListener(service) {
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
        messageBus.register(UserFollowEvent::class.java) {
            println("$this Received event: $it")
            if (it is UserFollowEvent) onUserFollowEvent(it)
        }
    }
}
