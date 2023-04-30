package com.klimek.langsapp.service.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration(
    @Value("\${langsapp.consul.url}") val consulUrl: String,
    @Value("\${langsapp.consul.directory}") val directory: String
) {

    @Bean
    fun configProvider(): ConfigProvider = ConsulConfigProvider(consulUrl = consulUrl, directory = directory)
}
