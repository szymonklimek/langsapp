rootProject.name = "service"

include(":components:auth")
include(":components:auth-jwt")
include(":components:domain")
include(":components:events")
include(":components:messagebus")
include(":langsapp")
include(":user-commands")
include(":user-follow-commands")
include(":user-follow-query")
include(":user-profile-query")
include(":user-query")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
