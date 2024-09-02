plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.graalvm.buildtools.native")
}

dependencies {
    api(project(":components:auth-jwt"))
    api(project(":components:config"))
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.validation)

    testImplementation(libs.mockwebserver)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
