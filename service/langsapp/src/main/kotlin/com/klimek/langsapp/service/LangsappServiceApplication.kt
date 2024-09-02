package com.klimek.langsapp.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.PropertySource

@SpringBootApplication
@PropertySource(
    "classpath:auth.properties",
)
class LangsappServiceApplication

fun main(args: Array<String>) {
    runApplication<LangsappServiceApplication>(*args)
}
