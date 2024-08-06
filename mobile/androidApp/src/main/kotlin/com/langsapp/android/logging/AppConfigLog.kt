package com.langsapp.android.logging

import com.langsapp.config.AppConfig

object AppConfigLog : AppConfig.Log {
    override fun v(message: String) {
        Log.v(message)
    }

    override fun d(message: String) {
        Log.d(message)
    }

    override fun i(message: String) {
        Log.i(message)
    }

    override fun w(message: String) {
        Log.w(message)
    }

    override fun e(message: String) {
        Log.e(message)
    }

    override fun wtf(message: String) {
        Log.wtf(message)
    }
}
