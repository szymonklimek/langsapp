package com.langsapp.architecture

interface SideEffect

sealed interface CommonSideEffect : SideEffect {
    abstract class NavigationSideEffect(direction: Direction) : CommonSideEffect {
        enum class Direction { BACKWARD, FORWARD }
    }
    data class ShowPopUpMessage(val message: String) : CommonSideEffect
}
