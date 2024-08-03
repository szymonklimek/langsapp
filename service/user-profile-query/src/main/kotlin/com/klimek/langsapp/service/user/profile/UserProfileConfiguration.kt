package com.klimek.langsapp.service.user.profile

import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.profile.event.MessageBusEventsListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserProfileConfiguration {

    @Bean
    fun eventsListener(
        messageBus: MessageBus,
        service: UserProfileQueryService,
    ) = MessageBusEventsListener(
        messageBus = messageBus,
        service = service,
    )
}
