import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.graalvm.buildtools.native")

    id("org.openapi.generator")
}

version = findProperty("scm.commit.hash") ?: error("Missing current commit hash")
group = findProperty("package.group") ?: error("Missing package group")

dependencies {
    api(project(":components:auth"))
    api(project(":components:events"))
    api(project(":components:messagebus"))
    api(libs.kotlin.stdlib.jdk8)
    api(libs.kotlin.reflect)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.reactor)

    api(libs.spring.boot.starter.webflux)

    api(libs.jackson.datatype.jsr310)
    api(libs.jackson.module.kotlin)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = libs.versions.jvm.target.get()
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// region Open API code generation

val generatedCodeDirectoryPath = "$buildDir${File.separator}open-api-generated"
val apiSpecPath = "${findProperty("open.api.directory.public")}user_profile_query_api.yaml"

tasks.openApiGenerate {
    setProperty("generatorName", "kotlin-spring")
    setProperty("validateSpec", true)
    setProperty("inputSpec", apiSpecPath)
    setProperty("outputDir", generatedCodeDirectoryPath)
    setProperty(
        "configOptions",
        mapOf(
            "interfaceOnly" to "true",
            "annotationLibrary" to "none",
            "documentationProvider" to "none",
            "useBeanValidation" to "false",
            "useSpringBoot3" to "true",
            "reactive" to "true",
            "packageName" to "com.klimek.langsapp.service.user.profile.query.generated",
            "modelPackage" to "com.klimek.langsapp.service.user.profile.query.generated",
        ),
    )
}

tasks.apply {
    clean { finalizedBy(openApiGenerate) }
    compileJava { dependsOn(openApiGenerate) }
    compileKotlin { dependsOn(openApiGenerate) }
}

sourceSets[SourceSet.MAIN_SOURCE_SET_NAME].java {
    srcDir(generatedCodeDirectoryPath + File.separator + "src" + File.separator + "main" + File.separator + "kotlin")
}

// endregion
