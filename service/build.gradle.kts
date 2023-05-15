import groovy.json.JsonSlurper

buildscript {
    project.extra.set("scm.commit.hash",
        Runtime.getRuntime().exec("git rev-parse --verify --short HEAD").apply { waitFor() }
            .inputStream.reader().readText().replace("\n", "")
    )
    project.extra.set("package.group", "com.klimek.langsapp")
    project.extra.set(
        "open.api.directory.public",
        rootDir.parentFile.absolutePath + File.separator + "api" + File.separator + "public" + File.separator
    )
}

plugins {
    kotlin("jvm") version "1.7.22" apply false
    kotlin("plugin.spring") version "1.7.22" apply false

    id("org.springframework.boot") version "3.0.5" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false

    id("org.graalvm.buildtools.native") version "0.9.20" apply false

    id("org.openapi.generator") version "6.4.0" apply false
}

// region Local infrastructure

val dockerComposeInfraPath = projectDir.absolutePath + File.separator + "docker-compose-infra.yaml"
val configurationFilePath = projectDir.absolutePath + File.separator + "configuration-properties.json"
val consulKeyValueStoreUrl = "127.0.0.1:8500/v1/kv/"

val runLocalInfrastructure by tasks.registering {
    group = "local development"
    description = "Runs infrastructure required by service locally"

    doLast {
        exec {
            commandLine("docker")
            args("compose", "-f", dockerComposeInfraPath, "up")
        }
    }
}

val cleanLocalInfrastructure by tasks.registering {
    group = "local development"
    description = "Cleans up local infrastructure"

    doLast {
        exec {
            commandLine("docker")
            args("compose", "-f", dockerComposeInfraPath, "down")
        }
    }
}

val initConsulConfiguration by tasks.registering {
    group = "local development"
    description = "Initialize configuration in local consul container"

    doLast {
        File(configurationFilePath)
            .reader()
            .readText()
            .let { JsonSlurper().parseText(it) as Map<*, *> }
            .forEach { (key, value) ->
                println("Uploading key: '$key' with value: '$value'")
                exec {
                    commandLine("curl")
                    args("--request", "PUT", "--data", value, "$consulKeyValueStoreUrl$key")
                }
            }
    }
}

val createPostgresDatabaseSchema by tasks.registering {
    group = "local development"
    description = "Executes SQL scripts for database schemas creations"

    doLast {
        val containerName = "postgres"
        val sqlScripts = projectDir
            .walk()
            .filter { it.isDirectory && it.name == "sql" }
            .flatMap { it.walk().filter { file -> file.isFile && file.extension == "sql" } }
            .sortedBy { it.name }

        sqlScripts.forEach { sqlScript ->
            exec {
                println("Copying script ${sqlScript.name} into database container")
                commandLine("docker")
                args("cp", sqlScript.absolutePath, "$containerName:/")
            }
            exec {
                println("Executing script: $sqlScript")
                commandLine("docker")
                args(
                    "exec", "-i", containerName,
                    "psql", "-d", "postgres", "-Upostgres", "-f", sqlScript.name
                )
            }
            exec {
                println("Deleting $sqlScript from database docker container")
                commandLine("docker")
                args(
                    "exec", "-i", containerName,
                    "rm", sqlScript.name
                )
            }
        }
    }
}

val configureLocalInfrastructure by tasks.registering {
    group = "local development"
    description = """
        Performs all necessary tasks to configure local infrastructure. 
        Can be executed only after local infra is running, see ${runLocalInfrastructure.name} task. 
    """.trimIndent()

    dependsOn(initConsulConfiguration)
    dependsOn(createPostgresDatabaseSchema)
}

// endregion
