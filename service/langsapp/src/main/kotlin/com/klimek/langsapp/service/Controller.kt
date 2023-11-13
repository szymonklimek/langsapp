package com.klimek.langsapp.service

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/whatever"]
    )
    suspend fun getWhatever(): ResponseEntity<String> {
        logger.info("Get whatever")

        return ResponseEntity.ok("Whatever")
    }
}
