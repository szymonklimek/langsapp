
buildscript {
    project.extra.set(
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
    project.extra.set("package.group", "com.klimek.langsapp")
    project.extra.set(
        "open.api.directory.public",
        rootDir.parentFile.absolutePath + File.separator + "api" + File.separator + "public" + File.separator,
    )
}

plugins {
    kotlin("jvm") version "1.9.21" apply false
    kotlin("plugin.spring") version "1.9.21" apply false

    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false

    id("org.graalvm.buildtools.native") version "0.9.20" apply false

    id("org.openapi.generator") version "7.8.0" apply false
}
