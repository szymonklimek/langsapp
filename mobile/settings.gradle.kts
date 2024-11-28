pluginManagement {
    java.util
        .Properties()
        .apply {
            rootDir
                .listFiles { file -> file.extension == "properties" }
                ?.map { if (it.exists()) load(it.inputStream()) }
        }.forEach { key, value -> gradle.extra.set(key.toString(), value) }
    gradle.extra.set(
        "scm.commit.hash",
        Runtime
            .getRuntime()
            .exec("git rev-parse --verify --short HEAD")
            .apply { waitFor() }
            .inputStream
            .reader()
            .readText()
            .replace("\n", ""),
    )
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "mobile"
include(":androidApp")
include(":app")
include(":api-clients:user-commands")
include(":api-clients:user-profile-query")
