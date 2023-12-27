package com.atafelska.crosswordapplication.otel

import android.util.Log
import io.opentelemetry.sdk.common.CompletableResultCode
import io.opentelemetry.sdk.trace.data.SpanData
import io.opentelemetry.sdk.trace.export.SpanExporter

class DebuggableSpanExporter(
    private val delegateSpanExporter: SpanExporter
): SpanExporter {
    override fun export(spans: MutableCollection<SpanData>): CompletableResultCode {
        Log.d("DebuggableSpanExporter", "export() called with spans: $spans")
        return delegateSpanExporter.export(spans)
    }

    override fun flush(): CompletableResultCode {
        Log.d("DebuggableSpanExporter", "flush() called")
        return delegateSpanExporter.flush()
    }

    override fun shutdown(): CompletableResultCode {
        Log.d("DebuggableSpanExporter", "shutdown() called")
        return delegateSpanExporter.shutdown()
    }
}
