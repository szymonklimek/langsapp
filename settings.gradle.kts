rootProject.name = "langsapp"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include(":api")
include("mobile-test")
includeBuild("mobile")
includeBuild("service")
