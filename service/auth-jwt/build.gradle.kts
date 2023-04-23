plugins {
    kotlin("jvm")
}

dependencies {
    api(libs.kotlin.stdlib.jdk8)
    api(libs.arrow.core)
    api(libs.auth0.java.jwt)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
