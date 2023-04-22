rootProject.name = "langsapp"

include(":langsapp")
include(":users-commands")
include(":user-follow-commands")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
