package com.klimek.langsapp.service.observability

import io.opentelemetry.instrumentation.annotations.WithSpan
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
/**
 * TODO https://github.com/szymonklimek/langsapp/issues/21
 * Find the solution to make functions annotated with '@WithSpan' produces proper spans as part of single trace
 */
class ObservabilityTestController: Logging {

    @RequestMapping(
        method = [RequestMethod.GET],
        value = ["/observability"]
    )
    suspend fun observability(): ResponseEntity<String> {
        logger().info("Observability test start")
        function1()
        function2()
        function3()
        function4()
        coroutineScope {
            launch { function2() }
            launch { function1() }
        }
        logger().info("Observability test end")
        return ResponseEntity.ok("Observability test finished")
    }

    @WithSpan
    suspend fun function1() {
        logger().info("function1 start")
        delay(230)
        logger().info("function1 end")
    }

    @WithSpan
    suspend fun function2() {
        logger().info("function2 start")
        delay(240)
        logger().info("function2 end")
    }

    @WithSpan
    suspend fun function3() {
        logger().info("function3 start")
        delay(210)
        logger().info("function3 end")
    }

    @WithSpan
    suspend fun function4() {
        logger().info("function4 start")
        delay(510)
        logger().info("function4 end")
    }
}