package com.langsapp.android

import android.app.Application
import com.langsapp.BuildConfig
import com.langsapp.android.logging.AppConfigLog
import com.langsapp.android.logging.Log
import com.langsapp.config.AppConfig
import com.langsapp.config.KeyValueStorage
import com.langsapp.observability.Observability
import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.SpanContext
import io.opentelemetry.api.trace.TraceFlags
import io.opentelemetry.api.trace.TraceState
import io.opentelemetry.context.Context
import io.opentelemetry.context.propagation.ContextPropagators

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
                override fun getAll(): Map<String, String> = map
                override fun set(key: String, value: String) = map.set(key, value)
                override fun remove(key: String) {
                    map.remove(key)
                }
            },
            devOptionsEnabled = com.langsapp.android.app.BuildConfig.DEBUG,
            observability = object : Observability {
                override fun startTrace(traceId: String, spanName: String, attributes: Map<String, String>) {
                    Log.d("Start trace: $traceId, span: $spanName, attributes: $attributes")
                }

                override fun endTrace(traceId: String) {
                    Log.d("End trace: $traceId")
                }

                override fun startSpan(traceId: String, spanName: String, attributes: Map<String, String>) {
                    Log.d("Start span: $traceId, span: $spanName, attributes: $attributes")
                }

                override fun endSpan(traceId: String, spanId: String) {
                    Log.d("End span: $traceId, span: $spanId")
                }

                override fun sendLog() {
                    Log.d("Send log")
                }
                //                override fun startTrace(name: String) {
//
//                    GlobalOpenTelemetry.set(OpenTelemetry.propagating(listOf(ContextPropagators.noop())))
//                    val span = GlobalOpenTelemetry.get()
//                        .tracerBuilder("")
//                        .build()
//                        .spanBuilder("")
//                        .startSpan()
//
//                    SpanContext.create(
//                        "traceId",
//                        "spanId",
//                        TraceFlags.getDefault(),
//                        TraceState.getDefault()
//                    )
//
//                    Span.wrap(SpanContext.create())
//
//
//                    Span.fromContext(Context.current())
//
//                }
//
//                override fun endTrace(name: String) {
//
//                }
//
//                override fun startSpan(name: String) {
            },
        )
    }
}
