package com.langsapp

import com.langsapp.config.AppConfig
import kotlinx.datetime.Clock
import kotlin.test.BeforeTest

open class BaseTest {
    @BeforeTest
    fun beforeTest() {
        AppConfig.init(
            log = object : AppConfig.Log {
                override fun v(message: String) {
                    println("${Clock.System.now()} v: $message")
                }

                override fun d(message: String) {
                    println("${Clock.System.now()} d: $message")
                }

                override fun i(message: String) {
                    println("${Clock.System.now()} i: $message")
                }

                override fun w(message: String) {
                    println("${Clock.System.now()} w: $message")
                }

                override fun e(message: String) {
                    println("${Clock.System.now()} e: $message")
                }

                override fun wtf(message: String) {
                    println("${Clock.System.now()} wtf: $message")
                }
            },
            keyValueStorage = InMemoryKeyValueStorage(),
        )
    }
}
