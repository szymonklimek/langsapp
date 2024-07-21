package com.langsapp.android.logging

object Log {

    private const val TAG = "Langsapp"

    @JvmStatic
    fun v(message: String) {
        val logMessage = getLogMessage(message)
        android.util.Log.v(TAG, logMessage)
    }

    @JvmStatic
    fun d(message: String) {
        val logMessage = getLogMessage(message)
        android.util.Log.d(TAG, logMessage)
    }

    @JvmStatic
    fun i(message: String) {
        val logMessage = getLogMessage(message)
        android.util.Log.i(TAG, logMessage)
    }

    @JvmStatic
    fun w(message: String) {
        val logMessage = getLogMessage(message)
        android.util.Log.w(TAG, logMessage)
    }

    @JvmStatic
    fun e(message: String) {
        val logMessage = getLogMessage(message)
        android.util.Log.e(TAG, logMessage)
    }

    @JvmStatic
    fun wtf(message: String) {
        val logMessage = getLogMessage(message)
        android.util.Log.wtf(TAG, logMessage)
    }

    private fun getLogMessage(message: String): String =
        runCatching {
            Thread.currentThread().stackTrace
                .drop(3)
                .first { !it.className.contains("Log") }
        }.mapCatching {
            "(${it.fileName}:${it.lineNumber}) ${it.methodName} @${Thread.currentThread().name} $message"
        }.getOrElse { message }
}
