import com.android.build.gradle.internal.tasks.factory.dependsOn

buildscript {
    project.extra.set(
        "open.api.directory.public",
        rootDir.parentFile.absolutePath + File.separator + "api" + File.separator + "public" + File.separator,
    )
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.21" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21" apply false

    id("com.android.application") version "8.2.0" apply false
    id("com.android.library") version "8.2.0" apply false
    id("org.openapi.generator") version "7.8.0"
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// region Open API code generation
val publicOpenApiSpecDirectory = "${findProperty("open.api.directory.public")}"
val generatedCodeOutputDirectory = "${layout.projectDirectory.asFile}${File.separator}api-clients"

data class ApiSpec(
    val gradleProjectName: String,
    val name: String,
    val directoryPath: String = publicOpenApiSpecDirectory,
    val specFileName: String,
    val generatorType: String = "kotlin",
    val groupId: String = "com.klimek.langsapp.openapi.generated",
    val packageName: String,
    val modelPackageName: String,
    val config: Map<String, String> =
        mapOf(
            "library" to "multiplatform",
            "dateLibrary" to "kotlinx-datetime",
            "omitGradlePluginVersions" to "true",
            "omitGradleWrapper" to "true",
            "useSettingsGradle" to "false",
            "sourceFolder" to "src/commonMain/kotlin",
        ),
)

/**
 * List of all api specs to generate
 */
val openApiSpecifications =
    listOf(
        ApiSpec(
            gradleProjectName = "user-commands",
            name = "User Commands API",
            specFileName = "user_commands_api.yaml",
            packageName = "com.klimek.langsapp.openapi.generated.user.commands",
            modelPackageName = "com.klimek.langsapp.openapi.generated.user.commands.model",
        ),
        ApiSpec(
            gradleProjectName = "user-profile-query",
            name = "User Profile Query API",
            specFileName = "user_profile_query_api.yaml",
            packageName = "com.klimek.langsapp.openapi.generated.user.profile.query",
            modelPackageName = "com.klimek.langsapp.openapi.generated.user.profile.query.model",
        ),
    )

// Iterate over the api list and register them as generator tasks
val regenerateOpenApiCode =
    tasks.register("regenerateOpenApiCode") {
        group = "openapi tools"
        description = "Re-generate the client code for all Open APIs specifications"
    }
openApiSpecifications.forEach { api ->
    val taskApiName = api.name.replace(" ", "")
    val apiOutputDir = "${generatedCodeOutputDirectory}${File.separator}${api.gradleProjectName}"
    val generateTask =
        tasks.register("generate$taskApiName", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
            group = "openapi tools"
            description = "Generate the code for ${api.name}"

            println("Starting Open API generation of: ${api.directoryPath}${File.separator}${api.specFileName}")
            generatorName.set(api.generatorType)
            inputSpec.set("${api.directoryPath}${File.separator}${api.specFileName}")
            outputDir.set(apiOutputDir)
            groupId.set(api.groupId)
            packageName.set(api.packageName)
            apiPackage.set(api.packageName)
            modelPackage.set(api.modelPackageName)
            configOptions.set(api.config)
        }
    val cleanTask =
        tasks.register("clean${taskApiName}GeneratedCode") {
            group = "openapi tools"
            doLast {
                delete(apiOutputDir)
            }
        }
    val regenerateTask =
        tasks.register("regenerate$taskApiName") {
            group = "openapi tools"
            dependsOn(cleanTask, generateTask)
        }
    regenerateOpenApiCode.dependsOn(regenerateTask)
}

// endregion
