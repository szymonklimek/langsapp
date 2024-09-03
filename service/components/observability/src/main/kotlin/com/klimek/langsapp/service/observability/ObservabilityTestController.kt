package com.klimek.langsapp.service.observability

import io.opentelemetry.api.logs.Logger
import io.opentelemetry.api.trace.Tracer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class ObservabilityTestController(
    private val logger: Logger,
    private val tracer: Tracer
) {

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/observability"]
    )
    suspend fun observability(): ResponseEntity<String> {
        logger
            .logRecordBuilder()
            .setBody("Observability test start- custom log").emit()
        function1()

        coroutineScope {
            launch { function3() }
            launch { function4() }
        }
        function2()
        function3()
        function4()
        coroutineScope {
            launch { function2() }
            launch { function1() }
            function3()
        }
        return ResponseEntity.ok("Observability test finished")
    }

    suspend fun function1() {
        withSpan(tracer, "function1") {
            delay(230)
        }
    }

    suspend fun function2() {
        withSpan(tracer, "function2") {
            delay(240)
        }
    }

    suspend fun function3() {
        withSpan(tracer, "function3") {
            delay(210)
        }
    }

    suspend fun function4() {
        withSpan(tracer, "function4") {
            delay(510)
        }
    }
}