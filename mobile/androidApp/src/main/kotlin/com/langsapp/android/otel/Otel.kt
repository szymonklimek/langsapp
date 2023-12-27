package com.atafelska.crosswordapplication.otel

import com.atafelska.crosswordapplication.common.AppInformation
import com.atafelska.crosswordapplication.network.Environment
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.trace.TracerProvider
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor


class Otel(
    environment: Environment,
    appInformation: AppInformation
) : OpenTelemetry {
    private val openTelemetry: OpenTelemetry =
        OpenTelemetrySdk
            .builder()
            .setTracerProvider(
                SdkTracerProvider.builder()
                    .setResource(
                        Resource.getDefault()
                            .toBuilder()
                            .put("service.name", appInformation.appTechnicalName)
                            .put("app.id", appInformation.appId)
                            .put("app.version", appInformation.appVersion)
                            .build()
                    )
                    .addSpanProcessor(DebuggableSpanExporter(
                        delegateSpanExporter = OtlpGrpcSpanExporter
                            .builder()
                            .setEndpoint(environment.otelCollectorUrl)
                            .build()
                    ).let {
                        DebuggableSpanProcessor(
                            delegateSpanProcessor = BatchSpanProcessor
                                .builder(it)
                                .build()
                        )
                    })
                    .build()

            )
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .build()

    override fun getTracerProvider(): TracerProvider = openTelemetry.tracerProvider

    override fun getPropagators(): ContextPropagators = openTelemetry.propagators
}
