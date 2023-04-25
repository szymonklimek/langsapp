package com.klimek.langsapp.service.user.commands

import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.commands.event.MessageBusUserEventsPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserCommandsConfiguration {

    @Bean
    fun userEventsPublisher(messageBus: MessageBus) = MessageBusUserEventsPublisher(messageBus)
}
