package com.klimek.langsapp.service.user.query

import com.klimek.langsapp.service.config.ConfigProvider
import com.klimek.langsapp.service.messagebus.MessageBus
import com.klimek.langsapp.service.user.query.event.MessageBusEventsListener
import com.klimek.langsapp.service.user.query.storage.UserQueryDatabase
import com.klimek.langsapp.service.user.query.storage.UserQueryRepository
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserQueryConfiguration {

    @Bean
    fun eventsPublisher(
        messageBus: MessageBus,
        repository: UserQueryRepository
    ) = MessageBusEventsListener(messageBus = messageBus, repository = repository)

    @Bean
    fun userQueryDataSource(configProvider: ConfigProvider) = UserQueryDatabase(
        PGSimpleDataSource().apply {
            setURL(configProvider.getValue("user.query.database.connection.url"))
        })
}
