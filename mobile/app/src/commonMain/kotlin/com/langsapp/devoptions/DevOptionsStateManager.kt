package com.langsapp.devoptions

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.StateManager

class DevOptionsStateManager(
    repository: DevOptionsRepository,
) : StateManager<DevOptionsState, DevOptionsAction>(
    initialState = DevOptionsState.Loading,
    initialSideEffects = ArrayDeque(listOf(DevOptionsSideEffect.LoadOptions)),
    handleAction = { _, action ->
        when (action) {
            is DevOptionsAction.OptionsLoaded -> ActionResult(
                newState = DevOptionsState.Loaded(
                    apiEnvironments = action.apiEnvironments,
                    selectedEnvironment = action.selectedEnvironment,
                ),
            )

            is DevOptionsAction.EnvironmentChanged -> ActionResult(
                sideEffects = listOf(DevOptionsSideEffect.ChangeEnvironment(selectedEnvironment = action.selectedEnvironment)),
            )

            DevOptionsAction.BackTapped -> ActionResult(
                sideEffects = listOf(CommonSideEffect.Exit),
            )
        }
    },
    handleSideEffect = {
        when (it) {
            DevOptionsSideEffect.LoadOptions -> DevOptionsAction.OptionsLoaded(
                apiEnvironments = repository.getPossibleEnvironments(),
                selectedEnvironment = repository.getSelectedEnvironment(),
            )
            is DevOptionsSideEffect.ChangeEnvironment -> {
                repository.saveSelectedEnvironment(it.selectedEnvironment)
                DevOptionsAction.OptionsLoaded(
                    apiEnvironments = repository.getPossibleEnvironments(),
                    selectedEnvironment = repository.getSelectedEnvironment(),
                )
            }
            else -> null
        }
    },
)
