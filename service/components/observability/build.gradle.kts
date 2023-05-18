import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.graalvm.buildtools.native")
}

dependencies {
    api(project(":components:config"))
    api(libs.spring.boot.starter)
    api(libs.spring.boot.starter.actuator)

    implementation(libs.micrometer.tracing.bridge.otel)
    implementation(libs.otel.exporter.otlp)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = libs.versions.jvm.target.get()
    }
}
