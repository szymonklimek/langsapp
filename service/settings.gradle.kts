rootProject.name = "langsapp"

include(":components:auth")
include(":components:auth-jwt")
include(":components:config")
include(":components:events")
include(":components:eventsstore")
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
