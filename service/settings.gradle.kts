rootProject.name = "langsapp"

include(":components:auth")
include(":components:auth-jwt")
include(":components:config")
include(":components:events")
include(":components:messagebus")
include(":langsapp")
include(":user-commands")
include(":user-follow-commands")
include(":user-profile-query")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
