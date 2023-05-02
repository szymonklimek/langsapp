package com.klimek.langsapp.service.user.query

import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.query.event.MessageBusEventsListener
import com.klimek.langsapp.service.user.query.storage.UserQueryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserQueryConfiguration {

    @Bean
    fun eventsPublisher(
        messageBus: MessageBus,
        repository: UserQueryRepository
    ) = MessageBusEventsListener(messageBus = messageBus, repository = repository)
}
