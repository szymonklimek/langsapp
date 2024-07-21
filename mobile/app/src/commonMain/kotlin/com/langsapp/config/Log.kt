package com.langsapp.config

object Log {
    fun v(message: String) {
        AppConfig.log.v(message)
    }
    fun d(message: String) {
        AppConfig.log.d(message)
    }
    fun i(message: String) {
        AppConfig.log.i(message)
    }
    fun w(message: String) {
        AppConfig.log.w(message)
    }
    fun e(message: String) {
        AppConfig.log.e(message)
    }
    fun wtf(message: String) {
        AppConfig.log.wtf(message)
    }
}
