buildscript {

    // region Builds scripts constants

    val containerRegistryPropertyKey = "container.registry.url"
    val localPropertiesFilename = "local.properties"

    // endregion

    java.util.Properties()
        .apply {
            file(localPropertiesFilename).run { if (exists()) load(reader()) }
        }
        .forEach { project.extra.set(it.key.toString(), it.value) }

    findProperty(containerRegistryPropertyKey)
        ?: println(
            """
            _____________________________________
            IMPORTANT: Container Registry URL is missing. 
            Provide '$containerRegistryPropertyKey' in project properties, for example in 'local.properties' file.
            _____________________________________
            """.trimIndent(),
        )
}

plugins {
    id("org.openapi.generator") version "6.4.0" apply false
    id("com.diffplug.spotless") version "6.25.0"
}

// region Spotless configuration

spotless {
    val excludedDirectories =
        listOf(
            "**/.gradle/**",
            "**/build/generated/**",
            "**/build/open-api-generated/**",
        )

    kotlin {
        target(
            fileTree(".") {
                include("**/*.kt")
                exclude(excludedDirectories)
            },
        )
        ktlint()
            .editorConfigOverride(
                mapOf(
                    // Android Jetpack Compose "Composable functions" starts with capital letter conventionally
                    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                ),
            )
    }
    kotlinGradle {
        target(
            fileTree(".") {
                include("**/*.gradle.kts")
                exclude(excludedDirectories)
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
