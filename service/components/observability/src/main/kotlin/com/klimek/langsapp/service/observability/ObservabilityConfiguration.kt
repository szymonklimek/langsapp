package com.klimek.langsapp.service.observability

import com.klimek.langsapp.service.config.ConfigProvider
import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.logs.Logger
import io.opentelemetry.api.logs.LoggerProvider
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.TracerProvider
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.resources.Resource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.web.server.CoWebFilter
import org.springframework.web.server.CoWebFilterChain
import org.springframework.web.server.ServerWebExchange

@Configuration
class ObservabilityConfiguration {

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


//                it?.setAttribute()
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

    @Bean
    fun observabilityResource(environment: Environment): Resource =
        Resource
            .builder()
            .put("service.name", environment["service.name"].toString())
            .build()

    @Bean
    fun tracer(tracerProvider: TracerProvider, environment: Environment): Tracer =
        tracerProvider.get(environment["service.name"] ?: "")

    @Bean
    fun logger(loggerProvider: LoggerProvider, environment: Environment): Logger =
        loggerProvider.get(environment["service.name"] ?: "")

    @Bean
    fun logExporter(
        configProvider: ConfigProvider
    ): OtlpGrpcLogRecordExporter = OtlpGrpcLogRecordExporter.builder()
        .setEndpoint(configProvider.getValue("otel.collector.url"))
        .build()

    @Bean
    fun spanExporter(
        configProvider: ConfigProvider
    ): OtlpGrpcSpanExporter = OtlpGrpcSpanExporter.builder()
        .setEndpoint(configProvider.getValue("otel.collector.url"))
        .build()

}
