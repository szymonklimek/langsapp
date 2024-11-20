package com.langsapp.observability

interface Observability {
    fun startTrace(name: String)

    fun endTrace(name: String)

    fun startSpan(name: String)

    fun endSpan(name: String)

    fun sendLog()
}
