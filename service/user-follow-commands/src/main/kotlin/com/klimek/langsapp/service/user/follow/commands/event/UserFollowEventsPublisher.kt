package com.klimek.langsapp.service.user.follow.commands.event

import com.klimek.langsapp.events.follow.UserFollowEvent


interface UserFollowEventsPublisher {
    fun sendUserFollowEvent(event: UserFollowEvent)
}
