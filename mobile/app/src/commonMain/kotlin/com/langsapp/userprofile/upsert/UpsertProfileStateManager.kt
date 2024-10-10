package com.langsapp.userprofile.upsert

import com.langsapp.architecture.ActionResult
import com.langsapp.architecture.CommonSideEffect
import com.langsapp.architecture.StateManager
import com.langsapp.config.Log
import com.langsapp.home.onboarding.UserProfileRepository
import com.langsapp.model.UserProfile

class UpsertProfileStateManager(
    private val currentProfile: UserProfile?,
    private val userProfileRepository: UserProfileRepository,
) : StateManager<UpsertProfileState, UpsertProfileAction>(
    initialState = UpsertProfileState.Input(
        currentProfile = currentProfile,
    ),
    handleAction = { currentState, action ->
        when (action) {
            UpsertProfileAction.BackTapped ->
                ActionResult(sideEffects = listOf(CommonSideEffect.Exit))

            is UpsertProfileAction.ConfirmTapped -> when (currentState) {
                is UpsertProfileState.Input ->
                    if (
                        action.newUsername.isNotBlank() && action.newUsername != currentProfile?.username
                    ) {
                        ActionResult(
                            newState = UpsertProfileState.Loading,
                            sideEffects = listOf(
                                UpsertProfileSideEffect.ConfirmProfileUpsert(
                                    username = action.newUsername,
                                ),
                            ),
                        )
                    } else {
                        ActionResult(
                            // TODO Replace with correctly localised text
                            sideEffects = listOf(CommonSideEffect.ShowPopUpMessage("Invalid input. TODO Replace me")),
                        )
                    }

                else -> ActionResult()
            }

            is UpsertProfileAction.ProfileUpdateFailed -> ActionResult(
                sideEffects = listOf(
                    // TODO Replace with correctly localised text
                    // TODO Handle possible "Duplicate username" or "Token expired" errors
                    CommonSideEffect.ShowPopUpMessage("Profile update failed"),
                ),
            )

            UpsertProfileAction.ProfileUpdateSucceeded -> ActionResult(
                sideEffects = listOf(
                    // TODO Replace with correctly localised text
                    CommonSideEffect.ShowPopUpMessage("Profile saved successfully"),
                    CommonSideEffect.Exit,
                ),
            )
        }
    },
    handleSideEffect = {
        when (it) {
            is UpsertProfileSideEffect.ConfirmProfileUpsert ->
                runCatching {
                    userProfileRepository.updateUsername(it.username)
                }
                    .fold(
                        onSuccess = {
                            UpsertProfileAction.ProfileUpdateSucceeded
                        },
                        onFailure = { reason ->
                            Log.e("Failed to update profile. Reason: $reason")
                            UpsertProfileAction.ProfileUpdateFailed(reason = reason)
                        },
                    )

            else -> null
        }
    },
)
