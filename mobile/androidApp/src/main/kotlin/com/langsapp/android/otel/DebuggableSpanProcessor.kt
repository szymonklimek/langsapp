package com.atafelska.crosswordapplication.otel

import android.util.Log
import io.opentelemetry.context.Context
import io.opentelemetry.sdk.trace.ReadWriteSpan
import io.opentelemetry.sdk.trace.ReadableSpan
import io.opentelemetry.sdk.trace.SpanProcessor

class DebuggableSpanProcessor(
    private val delegateSpanProcessor: SpanProcessor
) : SpanProcessor {
    override fun onStart(parentContext: Context, span: ReadWriteSpan) {
        Log.d(
            "DebuggableSpanProcessor",
            "onStart() called with parentContext: $parentContext, span = $span"
        )
    }

    override fun isStartRequired(): Boolean {
        Log.d(
            "DebuggableSpanProcessor",
            "isStartRequired() called"
        )
        val isStartRequired = delegateSpanProcessor.isStartRequired
        Log.d(
            "DebuggableSpanProcessor",
            "isStartRequired() returning: $isStartRequired"
        )
        return isStartRequired
    }

    override fun onEnd(span: ReadableSpan) {
        Log.d(
            "DebuggableSpanProcessor",
            "onEnd() called with span = $span"
        )
        delegateSpanProcessor.onEnd(span)
    }

    override fun isEndRequired(): Boolean {
        Log.d(
            "DebuggableSpanProcessor",
            "isEndRequired() called"
        )
        val isEndRequired = delegateSpanProcessor.isEndRequired
        Log.d(
            "DebuggableSpanProcessor",
            "isEndRequired() returning: $isEndRequired"
        )
        return isEndRequired
    }
}
