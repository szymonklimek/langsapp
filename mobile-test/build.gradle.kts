plugins {
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(libs.appium.java)
    implementation(libs.bundles.cucumber)
    implementation(libs.bundles.junit)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }
}
