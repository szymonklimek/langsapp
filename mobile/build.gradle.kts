buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.android.tools.build:gradle:7.1.3")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.0" apply false

    id("com.android.application") version "7.4.0-rc01" apply false
    id("com.android.library") version "7.4.0-rc01" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}


allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
