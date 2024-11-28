import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.graalvm.buildtools.native")
}

version = findProperty("scm.commit.hash") ?: error("Missing current commit hash")
group = findProperty("package.group") ?: error("Missing package group")

dependencies {
    implementation(project(":components:auth"))
    implementation(project(":user-commands"))
    implementation(project(":user-follow-commands"))
    implementation(project(":user-profile-query"))

    testImplementation(libs.cucumber.java)
    testImplementation(libs.cucumber.junit.platform.engine)
    testImplementation(libs.cucumber.spring)
    testImplementation(libs.junit.platform.suite)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.testcontainers.junit.jupiter)
}

val dockerImageTag = "service-langsapp:$version"

tasks.named<BootBuildImage>("bootBuildImage") {
    imageName = dockerImageTag
}

val pushImageToRegistry by tasks.registering {
    group = "deployment"
    doLast {
        val containerRegistryUrl =
            gradle.extra.get("container.registry.url")
                ?: println(
                    """
                    _____________________________________
                    IMPORTANT: Container Registry URL is missing.
                    Provide 'container.registry.url' in project properties, for example in 'local.properties' file.
                    _____________________________________
                    """.trimIndent(),
                )
        val remoteImageUrl = "$containerRegistryUrl/$dockerImageTag"
        exec {
            commandLine("docker", "tag", dockerImageTag, remoteImageUrl)
        }
        exec {
            commandLine("docker", "push", remoteImageUrl)
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget =
            libs.versions.jvm.target
                .get()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
