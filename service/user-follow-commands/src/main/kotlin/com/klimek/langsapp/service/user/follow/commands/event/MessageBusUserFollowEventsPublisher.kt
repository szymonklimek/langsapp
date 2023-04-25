package com.klimek.langsapp.service.user.follow.commands.event

import com.klimek.langsapp.events.follow.UserFollowEvent
import com.klimek.langsapp.service.messagebus.MessageBus


class MessageBusUserFollowEventsPublisher(private val messageBus: MessageBus) : UserFollowEventsPublisher {
    override fun sendUserFollowEvent(event: UserFollowEvent) {
        messageBus.post(event)
    }
}
