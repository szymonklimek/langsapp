rootProject.name = "langsapp"

include(":auth")
include(":auth-jwt")
include(":config")
include(":langsapp")
include(":user-commands")
include(":user-follow-commands")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
