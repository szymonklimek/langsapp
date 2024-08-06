package com.langsapp.android

import android.app.Application
import com.langsapp.android.logging.AppConfigLog
import com.langsapp.config.AppConfig
import com.langsapp.config.KeyValueStorage

class LangsappApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfig.init(
            log = AppConfigLog,
            // TODO Replace with persistent file storage
            keyValueStorage = object : KeyValueStorage {
                private val map = mutableMapOf<String, String>()
                override fun get(key: String): String? = map[key]
                override fun set(key: String, value: String) = map.set(key, value)
                override fun remove(key: String) {
                    map.remove(key)
                }
            },
        )
    }
}
