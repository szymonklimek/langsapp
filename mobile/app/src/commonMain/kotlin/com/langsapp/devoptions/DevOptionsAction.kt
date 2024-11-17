package com.langsapp.devoptions

import com.langsapp.architecture.Action
import com.langsapp.devoptions.model.ApiEnvironment

sealed class DevOptionsAction : Action {
    data object BackTapped : DevOptionsAction()
    data class OptionsLoaded(
        val apiEnvironments: List<ApiEnvironment>,
        val selectedEnvironment: ApiEnvironment,
    ) : DevOptionsAction()
    data class EnvironmentChanged(val selectedEnvironment: ApiEnvironment) : DevOptionsAction()
}
