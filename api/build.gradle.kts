
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
        val containerRegistryUrl = findProperty("container.registry.url")
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
