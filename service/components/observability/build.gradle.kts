import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.graalvm.buildtools.native")
}

dependencies {
    implementation(project(":components:config"))
    api(libs.spring.boot.starter)
    api(libs.kotlin.stdlib.jdk8)
    api(libs.otel.api)
    api(libs.otel.spring)
    api(libs.otel.exporter.otlp)
    api("io.opentelemetry:opentelemetry-extension-kotlin")

    // Dependencies for test controller
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.spring.boot.starter.webflux)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = libs.versions.jvm.target.get()
    }
}
