rootProject.name = "langsapp"

include(":auth-jwt")
include(":config")
include(":langsapp")
include(":users-commands")
include(":user-follow-commands")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
