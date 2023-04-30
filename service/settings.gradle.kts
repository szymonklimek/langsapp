rootProject.name = "langsapp"

include(":components:auth")
include(":components:auth-jwt")
include(":components:config")
include(":langsapp")
include(":user-commands")
include(":user-follow-commands")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
