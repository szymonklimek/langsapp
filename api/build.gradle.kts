import org.openapitools.generator.gradle.plugin.tasks.ValidateTask

plugins {
    id("org.openapi.generator")
}

// region Documentation docker image building and publishing

val imageName = "${rootProject.name}-open-api-docs"

val rebuildDockerImage by tasks.registering {
    group = "docs deployment"
    description = "Builds new docker image, removing previous one if it exists"

    doLast {
        println("Removing image: $imageName")
        runCatching {
            exec {
                commandLine("docker")
                args("image", "rm", imageName)
            }
        }.onFailure {
            println("Failed removing image: $imageName. Reason: $it")
        }

        println("Building image: $imageName")
        exec {
            commandLine("docker")
            args("build", "-t", imageName, ".")
        }
    }
}

val pushDockerImage by tasks.registering {
    group = "docs deployment"
    description = "Uploads docker image to container registry "

    doLast {
        val containerRegistryUrl =
            findProperty("container.registry.url")
                ?: error("Missing container registry url")
        val imageUrl = "$containerRegistryUrl/$imageName"

        println("Tagging image: $imageUrl")
        exec {
            commandLine("docker")
            args("tag", imageName, imageUrl)
        }

        println("Publishing image: $imageUrl")
        exec {
            commandLine("docker")
            args("push", imageUrl)
        }
    }
}

// endregion

// region Open API specs validation

val openApiValidationGroup = "open api validation"
val apiDirectoryPath = projectDir.absolutePath ?: error("Invalid project path")

val validationTasks =
    listOf(
        "UserCommandsApi" to "$apiDirectoryPath/public/user_commands_api.yaml",
        "UserFollowCommandsApi" to "$apiDirectoryPath/public/user_follow_commands_api.yaml",
        "UserProfileApi" to "$apiDirectoryPath/public/user_profile_query_api.yaml",
    ).map {
        tasks.register("validate${it.first}", ValidateTask::class) {
            group = openApiValidationGroup
            recommend.set(false)
            inputSpec.set(it.second)
        }
    }

val validateOpenApiSpecs by tasks.registering {
    group = openApiValidationGroup
    validationTasks.forEach {
        dependsOn(it)
    }
}

// endregion
