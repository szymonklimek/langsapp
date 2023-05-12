package com.klimek.langsapp.service.user.follow.query

import com.klimek.langsapp.service.config.ConfigProvider
import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.follow.query.event.MessageBusEventsListener
import com.klimek.langsapp.service.user.follow.query.storage.UserFollowQueryDatabase
import com.klimek.langsapp.service.user.follow.query.storage.UserFollowQueryRepository
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserFollowQueryConfiguration {

    @Bean
    fun userFollowQueryEventListener(
        messageBus: MessageBus,
        repository: UserFollowQueryRepository
    ) = MessageBusEventsListener(messageBus = messageBus, repository = repository)

    @Bean
    fun userFollowQueryDataSource(configProvider: ConfigProvider) = UserFollowQueryDatabase(
        PGSimpleDataSource().apply {
            setURL(configProvider.getValue("user.follow.query.database.connection.url"))
        })
}
