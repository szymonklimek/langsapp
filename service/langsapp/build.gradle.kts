import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation(project(":auth"))
    implementation(project(":config"))
    implementation(project(":user-commands"))
    implementation(project(":user-follow-commands"))
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
