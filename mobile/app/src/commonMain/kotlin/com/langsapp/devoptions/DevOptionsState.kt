package com.langsapp.devoptions

import com.langsapp.architecture.State
import com.langsapp.devoptions.model.ApiEnvironment

sealed class DevOptionsState : State {
    data object Loading : DevOptionsState()
    data class Loaded(
        val apiEnvironments: List<ApiEnvironment>,
        val selectedEnvironment: ApiEnvironment,
    ) : DevOptionsState()
}
