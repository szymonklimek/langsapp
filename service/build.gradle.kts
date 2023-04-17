buildscript {
    project.extra.set("scm.commit.hash",
        Runtime.getRuntime().exec("git rev-parse --verify --short HEAD").apply { waitFor() }
            .inputStream.reader().readText().replace("\n", "")
    )
    project.extra.set("package.group", "com.klimek.langsapp")
}

plugins {
    kotlin("jvm") version "1.7.22" apply false
    kotlin("plugin.spring") version "1.7.22" apply false

    id("org.springframework.boot") version "3.0.5" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false

    id("org.graalvm.buildtools.native") version "0.9.20" apply false

    id("org.openapi.generator") version "6.4.0" apply false
}
