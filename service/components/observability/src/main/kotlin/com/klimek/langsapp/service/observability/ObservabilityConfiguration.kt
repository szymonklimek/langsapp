package com.klimek.langsapp.service.observability

import com.klimek.langsapp.service.config.ConfigProvider
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.trace.export.SpanExporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObservabilityConfiguration {

    @Bean
    fun spanExporter(configProvider: ConfigProvider): SpanExporter =
        OtlpGrpcSpanExporter
            .builder()
            .setEndpoint(configProvider.getValue("otel.collector.endpoint.url"))
            .build()

}
