package com.klimek.langsapp.service.observability

import com.klimek.langsapp.service.config.ConfigProvider
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.logs.export.LogRecordExporter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Hooks

@Configuration
class ObservabilityConfiguration {

    companion object {
        init {
            Hooks.enableAutomaticContextPropagation()
        }
    }

    @Bean
    fun spanExporter(configProvider: ConfigProvider): OtlpGrpcSpanExporter =
        OtlpGrpcSpanExporter
            .builder()
            .setEndpoint(configProvider.getValue("otel.collector.endpoint.url"))
            .build()

    @Bean
    fun logExporter(configProvider: ConfigProvider): LogRecordExporter
      =  OtlpGrpcLogRecordExporter
            .builder()
            .setEndpoint(configProvider.getValue("otel.collector.endpoint.url"))
            .build()

}
