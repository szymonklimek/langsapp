
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.21" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21" apply false

    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false

    id("com.diffplug.spotless") version "6.25.0"
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// region Spotless configuration

spotless {
    kotlin {
        target(
            fileTree(".") {
                include("**/*.kt")
                exclude(
                    "**/.gradle/**",
                    "**/build/generated/**",
                )
            },
        )
        ktlint()
            .editorConfigOverride(
                mapOf(
                    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                ),
            )
    }
    kotlinGradle {
        target(
            fileTree(".") {
                include("**/*.gradle.kts")
                exclude("**/.gradle/**", "**/build/generated/**")
            },
        )
        trimTrailingWhitespace()
        ktlint()
    }
}

tasks.named("spotlessKotlin") {
    dependsOn("spotlessKotlinGradle")
}

// endregion
