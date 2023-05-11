package com.klimek.langsapp.service.user.follow.query

import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.follow.query.event.MessageBusEventsListener
import com.klimek.langsapp.service.user.follow.query.storage.UserFollowQueryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserFollowQueryConfiguration {

    @Bean
    fun userFollowQueryEventListener(
        messageBus: MessageBus,
        repository: UserFollowQueryRepository
    ) = MessageBusEventsListener(messageBus = messageBus, repository = repository)
}
