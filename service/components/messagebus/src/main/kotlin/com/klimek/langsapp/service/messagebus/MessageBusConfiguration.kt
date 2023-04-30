package com.klimek.langsapp.service.messagebus

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MessageBusConfiguration {

    @Bean
    fun messageBus(): MessageBus = MessageBus()
}
