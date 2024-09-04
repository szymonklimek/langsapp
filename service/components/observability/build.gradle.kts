import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.graalvm.buildtools.native")
}

dependencies {
    api(libs.spring.boot.starter)
    api(libs.kotlin.stdlib.jdk8)
    api(libs.otel.api)
    implementation(libs.otel.sdk)
    implementation(libs.otel.spring)
    implementation(libs.otel.exporter.otlp)
    implementation(libs.otel.extension.kotlin)

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
