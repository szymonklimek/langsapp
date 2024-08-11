package com.langsapp.content

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.StateManager
import com.langsapp.config.Log
import com.langsapp.model.LanguageSetting

class ManageContentStateManager(
    val languageSetting: LanguageSetting,
    private val manageContentRepository: ManageContentRepository,
) : StateManager<ManageContentState, ManageContentAction>(
    initialState = ManageContentState.Loading,
    initialSideEffects = ArrayDeque(listOf(ManageContentSideEffect.LoadContentData)),
    handleAction = { previousState, action ->
        when (action) {
            is ManageContentAction.DataLoaded -> ActionResult(
                newState = ManageContentState.Loaded(
                    hasContent = action.unitsCount > 0,
                ),
            )

            ManageContentAction.DataLoadingFailed -> ActionResult(
                newState = ManageContentState.Failure,
            )

            ManageContentAction.RetryTapped -> ActionResult(
                newState = ManageContentState.Loading,
                sideEffects = listOf(ManageContentSideEffect.LoadContentData),
            )

            ManageContentAction.DownloadUnitsTapped -> when (previousState) {
                is ManageContentState.Loaded -> ActionResult(
                    sideEffects = listOf(ManageContentSideEffect.DownloadUnits(previousState)),
                )
                else -> {
                    Log.e("Unexpected state: $previousState for action: $action")
                    ActionResult()
                }
            }

            ManageContentAction.BackTapped -> ActionResult(sideEffects = listOf(CommonSideEffect.Exit))
            is ManageContentAction.DownloadUnitsFailed -> ActionResult(
                newState = action.previousState,
                sideEffects = listOf(
                    // TODO Replace with correctly localised text
                    CommonSideEffect.ShowPopUpMessage("Something went wrong :/ "),
                ),
            )

            ManageContentAction.DownloadUnitsSucceeded -> ActionResult(
                sideEffects = listOf(
                    // TODO Replace with correctly localised text
                    CommonSideEffect.ShowPopUpMessage("Content downloaded success"),
                    CommonSideEffect.Exit,
                ),
            )
        }
    },
    handleSideEffect = {
        when (it) {
            is ManageContentSideEffect.LoadContentData ->
                manageContentRepository
                    .getAllUnits(languageSetting)
                    .fold(
                        onSuccess = { units ->
                            ManageContentAction.DataLoaded(unitsCount = units.size)
                        },
                        onFailure = {
                            ManageContentAction.DataLoadingFailed
                        },
                    )

            is ManageContentSideEffect.DownloadUnits ->
                manageContentRepository
                    .downloadUnits(languageSetting)
                    .fold(
                        onSuccess = {
                            ManageContentAction.DownloadUnitsSucceeded
                        },
                        onFailure = { _ ->
                            ManageContentAction.DownloadUnitsFailed(it.previousState)
                        },
                    )

            else -> null
        }
    },
)
