import java.text.SimpleDateFormat
import java.util.Date

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "0.1"
val buildProperties =
    mapOf(
        "APP_VERSION" to project.version,
        "BUILD_COMMIT_HASH" to gradle.extra.get("scm.commit.hash").toString(),
        "BUILD_TIME" to
            SimpleDateFormat("yyyyMMddHHmm")
                .format(Date())
                .substring(3)
                .toInt()
                .toString(),
        "APPAUTH_AUTHORIZATION_ENDPOINT" to gradle.extra.get("appauth.authorization.endpoint"),
        "APPAUTH_TOKEN_ENDPOINT" to gradle.extra.get("appauth.token.endpoint"),
        "APPAUTH_REGISTRATION_ENDPOINT" to gradle.extra.get("appauth.registration.endpoint"),
        "APPAUTH_ENDSESSION_ENDPOINT" to gradle.extra.get("appauth.endsession.endpoint"),
        "APPAUTH_CLIENT_ID" to gradle.extra.get("appauth.client.id"),
        "APPAUTH_REDIRECT_URI" to gradle.extra.get("appauth.redirect.uri"),
    )
val buildConfigGenerator by tasks.registering(Sync::class) {
    description = "Create shared build configuration class containing static properties"
    group = "build"
    outputs.upToDateWhen { false }
    val buildConfigFileContents: Provider<TextResource> =
        provider { buildProperties }
            .map { properties ->
                resources.text.fromString(
                    """
                    |package com.langsapp
                    |
                    |object BuildConfig {
                    |${
                        properties
                            .map { "    const val ${it.key} = \"${it.value}\"" }
                            .joinToString(separator = "\n")
                    }
                    |}
                    |
                    """.trimMargin(),
                )
            }

    from(buildConfigFileContents) {
        rename { "BuildConfig.kt" }
        into("com${File.separator}langsapp${File.separator}")
    }
    into(
        layout.buildDirectory.dir(
            "generated${File.separator}src${File.separator}commonMain${File.separator}kotlin",
        ),
    )
}

kotlin {
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Module containing business logic of Langsapp application"
        homepage = "TODO: Homepage link"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "app"
        }
    }

    sourceSets {
        val commonMain by getting {
            kotlin.srcDir(
                buildConfigGenerator.map { it.destinationDir },
            )
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
    }

    jvmToolchain(17)
}

android {
    namespace = "com.langsapp.android"
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 23
        targetSdk = 34
    }
}
