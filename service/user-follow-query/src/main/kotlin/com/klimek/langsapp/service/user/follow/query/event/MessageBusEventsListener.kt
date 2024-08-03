package com.klimek.langsapp.service.user.follow.query.event

import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.follow.query.storage.UserFollowQueryRepository

class MessageBusEventsListener(
    messageBus: MessageBus,
    repository: UserFollowQueryRepository,
) : EventsListener(repository) {
    init {
        println("Registering listener $this to event bus: $messageBus")
        messageBus.register(UserFollowEvent::class.java) {
            println("$this Received event: $it")
            if (it is UserFollowEvent) onUserFollowEvent(it)
        }
    }
}
