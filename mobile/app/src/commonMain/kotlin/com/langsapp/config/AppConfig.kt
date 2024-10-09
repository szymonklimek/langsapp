package com.langsapp.config

import com.langsapp.platform.randomUUID

object AppConfig {
    lateinit var log: Log
    lateinit var keyValueStorage: KeyValueStorage
    lateinit var uniqueInstallationId: String

    fun init(
        log: Log,
        keyValueStorage: KeyValueStorage,
    ) {
        this.log = log
        this.keyValueStorage = keyValueStorage
        this.uniqueInstallationId = "installation_id"
            .let { keyValueStorage.get(it) ?: randomUUID().apply { keyValueStorage.set(it, this) } }
    }

    interface Log {
        fun v(message: String)
        fun d(message: String)
        fun i(message: String)
        fun w(message: String)
        fun e(message: String)
        fun wtf(message: String)
    }
}