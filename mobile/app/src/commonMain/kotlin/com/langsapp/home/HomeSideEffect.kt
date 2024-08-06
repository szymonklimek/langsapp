package com.langsapp.home

import com.langsapp.architecture.SideEffect

sealed interface HomeSideEffect {
    data object FetchHomeDataSideEffect : SideEffect
    data class FetchLearnUnitsSideEffect(val count: Int) : SideEffect
}
