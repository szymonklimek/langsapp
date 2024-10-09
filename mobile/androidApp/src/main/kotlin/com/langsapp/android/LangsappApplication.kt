package com.langsapp.android

import android.app.Application
import com.langsapp.BuildConfig
import com.langsapp.android.logging.AppConfigLog
import com.langsapp.config.AppConfig
import com.langsapp.config.KeyValueStorage

class LangsappApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfigLog.d(
            "Starting application. " +
                "Version: ${BuildConfig.APP_VERSION}, " +
                "commit: ${BuildConfig.BUILD_COMMIT_HASH}, " +
                "built at: ${BuildConfig.BUILD_TIME}",
        )
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
