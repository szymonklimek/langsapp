package com.langsapp.observability

// A Span represents a single operation performed by a single component of the system.
//
// Inspired by: https://github.com/open-telemetry/opentelemetry-proto/blob/main/opentelemetry/proto/trace/v1/trace.proto
data class Span(
    val traceId: String,
    val spanId: String,
    val name: String,
    val kind: SpanKind,
    val startTimeUnixNano: Long,
    val endTimeUnixNano: Long,
)

enum class SpanKind {
    CLIENT, SERVER, PRODUCER, CONSUMER
}
