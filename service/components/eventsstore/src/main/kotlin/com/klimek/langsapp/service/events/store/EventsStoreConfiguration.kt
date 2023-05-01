package com.klimek.langsapp.service.events.store

import com.klimek.langsapp.service.config.ConfigProvider
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventsStoreConfiguration {
    @Bean
    fun eventsStoreDataSource(configProvider: ConfigProvider) = EventsStoreDatabase(
        PGSimpleDataSource().apply {
            setURL(configProvider.getValue("events.store.database.connection.url"))
        })
}