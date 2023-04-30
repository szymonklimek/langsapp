package com.klimek.langsapp.service.user.follow.commands

import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.follow.commands.event.MessageBusUserFollowEventsPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserFollowCommandsConfiguration {

    @Bean
    fun userFollowEventsPublisher(messageBus: MessageBus) = MessageBusUserFollowEventsPublisher(messageBus)
}
