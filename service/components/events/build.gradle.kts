import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    api(libs.kotlin.stdlib.jdk8)
    api(project(":components:domain"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget =
            libs.versions.jvm.target
                .get()
    }
}
