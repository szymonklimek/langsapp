rootProject.name = "langsapp"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":api")
includeBuild("mobile")
includeBuild("service")
