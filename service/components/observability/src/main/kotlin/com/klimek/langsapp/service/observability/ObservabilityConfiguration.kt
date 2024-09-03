package com.klimek.langsapp.service.observability

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.logs.Logger
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.logs.SdkLoggerProvider
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.server.CoWebFilter
import org.springframework.web.server.CoWebFilterChain
import org.springframework.web.server.ServerWebExchange

@Configuration
class ObservabilityConfiguration {

    @Value("\${otel.collector.url}")
    var otelCollectorUrl: String? = null

    @Bean
    fun openTelemetryResource(environment: Environment): Resource =
        Resource
            .builder()
            .put("service.name", environment["service.name"].toString())
            .build()

    @Bean
    fun openTelemetry(resource: Resource): OpenTelemetry =
        OpenTelemetrySdk
            .builder()
            .setTracerProvider(
                SdkTracerProvider
                    .builder()
                    .setResource(resource)
                    .addSpanProcessor(
                        BatchSpanProcessor
                            .builder(
                                OtlpGrpcSpanExporter
                                    .builder()
                                    .setEndpoint(otelCollectorUrl!!)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .setLoggerProvider(
                SdkLoggerProvider
                    .builder()
                    .setResource(resource)
                    .addLogRecordProcessor(
                        BatchLogRecordProcessor
                            .builder(
                                OtlpGrpcLogRecordExporter
                                    .builder()
                                    .setEndpoint(otelCollectorUrl!!)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build()

    @Bean
    fun tracer(openTelemetry: OpenTelemetry, environment: Environment): Tracer =
        openTelemetry.tracerProvider.get(environment["service.name"] ?: "")

    @Bean
    fun logger(openTelemetry: OpenTelemetry, environment: Environment): Logger =
        openTelemetry.logsBridge.get(environment["service.name"] ?: "")

    @Bean
    fun observabilityWebFilter(logger: Logger, tracer: Tracer): CoWebFilter = object : CoWebFilter() {
        override suspend fun filter(exchange: ServerWebExchange, chain: CoWebFilterChain) {
            withSpan(tracer, exchange.request.let { it.method.name() + " " + it.path.toString() }) {

                // Log request
                val requestAttributes = Attributes.builder()
                    .put("http_request_method", exchange.request.method.name())
                    .apply {
                        exchange.request.headers.forEach { key, value ->
                            put(AttributeKey.stringKey(key), value.firstOrNull() ?: "")
                        }
                    }

                logger.logRecordBuilder()
                    .setBody("${exchange.request}")
                    .setAllAttributes(requestAttributes.build())
                    .emit()


                chain.filter(exchange)

                // Log response
                logger.logRecordBuilder()
                    .setBody("${exchange.response}")
                    .apply {
                        setAttribute(
                            AttributeKey.stringKey("http_response_status"),
                            exchange.response.statusCode?.toString() ?: "-1"
                        )

                        exchange.response.headers.forEach { key, value ->
                            setAttribute(AttributeKey.stringKey(key), value.firstOrNull() ?: "")
                        }
                    }
                    .emit()
            }
        }
    }
}
