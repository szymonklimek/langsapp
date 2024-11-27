package com.langsapp.observability

interface Observability {
    fun startTrace(traceId: String, spanName: String, attributes: Map<String, String>)

    fun endTrace(traceId: String)

    fun startSpan(traceId: String, spanName: String, attributes: Map<String, String>)

    fun endSpan(traceId: String, spanId: String)

    fun sendLog()
}
