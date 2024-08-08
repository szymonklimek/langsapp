plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    testImplementation(libs.appium.java)
    testImplementation(libs.bundles.cucumber)
    testImplementation(libs.bundles.junit)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
