package com.klimek.langsapp.service.observability

import com.klimek.langsapp.service.config.ConfigProvider
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObservabilityConfiguration {

    @Bean
    fun logExporter(
        configProvider: ConfigProvider
    ): OtlpGrpcLogRecordExporter = OtlpGrpcLogRecordExporter.builder()
        .setEndpoint(configProvider.getValue("otel.collector.url"))
        .build()

}
