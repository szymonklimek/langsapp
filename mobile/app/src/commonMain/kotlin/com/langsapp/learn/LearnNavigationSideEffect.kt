package com.langsapp.learn

import com.langsapp.architecture.CommonSideEffect

sealed class LearnNavigationSideEffect(direction: Direction) : CommonSideEffect.NavigationSideEffect(direction) {
    data object Exit : LearnNavigationSideEffect(direction = Direction.BACKWARD)
}
