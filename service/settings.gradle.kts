rootProject.name = "langsapp"

include(":components:auth")
include(":components:auth-jwt")
include(":components:config")
include(":components:events")
include(":langsapp")
include(":user-commands")
include(":user-follow-commands")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
