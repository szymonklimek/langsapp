package com.klimek.langsapp.service.observability

import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanBuilder
import io.opentelemetry.api.trace.StatusCode
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.extension.kotlin.asContextElement
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.coroutineContext

/**
 * Executes [block] in a tracing span with optional SpanBuilder [parameters].
 *
 * [parameters] example: `parameters = { setParent(parentContext); addLink(span1.spanContext) }`
 *
 * The span will be
 * - a child of a parent context, if set via [parameters], or
 * - a child of the current span (from the current coroutine context), or
 * - a top-level span.
 *
 * Guidelines:
 * - [Trace Semantic Conventions](<https://opentelemetry.io/docs/reference/specification/trace/semantic_conventions/>)
 * - [Attribute Naming](<https://opentelemetry.io/docs/reference/specification/common/attribute-naming/>)
 */
suspend fun <Result> withSpan(
    tracer: Tracer,
    name: String,
    parameters: (SpanBuilder.() -> Unit)? = null,
    exceptionIsError: (Throwable) -> Boolean = { it !is CancellationException },
    block: suspend (span: Span?) -> Result
): Result {
    val span: Span = tracer.spanBuilder(name).run {
        if (parameters != null) parameters()
        coroutineContext[CoroutineName]?.let {
            setAttribute("coroutine.name", it.name)
        }
        setStartTimestamp(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        startSpan()
    }

    return withContext(span.asContextElement()) {
        try {
            block(span).also {
                span.setStatus(StatusCode.OK)
            }
        } catch (throwable: Throwable) {
            if (exceptionIsError(throwable)) {
                span.setStatus(StatusCode.ERROR)
                span.recordException(throwable)
            } else {
                span.addEvent(
                    "Completed with exception",
                    Attributes.builder()
                        .put("exception.type", throwable.javaClass.name)
                        .put("exception.message", throwable.message ?: "(none)")
                        .build()
                )
                span.setStatus(StatusCode.OK)
            }
            throw throwable
        } finally {
            span.end(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
        }
    }
}
