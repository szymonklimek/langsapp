package com.klimek.langsapp.service.user.query

import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.query.event.MessageBusEventsListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserQueryConfiguration {

    @Bean
    fun eventsPublisher(
        messageBus: MessageBus,
        service: UserQueryService,
    ) = MessageBusEventsListener(messageBus = messageBus, service = service)
}
