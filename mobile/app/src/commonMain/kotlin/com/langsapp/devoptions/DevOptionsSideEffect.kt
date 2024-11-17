package com.langsapp.devoptions

import com.langsapp.architecture.SideEffect
import com.langsapp.devoptions.model.ApiEnvironment

sealed class DevOptionsSideEffect : SideEffect {
    data object LoadOptions : DevOptionsSideEffect()
    data class ChangeEnvironment(val selectedEnvironment: ApiEnvironment) : DevOptionsSideEffect()
}
