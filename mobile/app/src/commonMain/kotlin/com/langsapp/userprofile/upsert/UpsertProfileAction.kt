package com.langsapp.userprofile.upsert

import com.langsapp.architecture.Action

sealed class UpsertProfileAction : Action {
    data class ConfirmTapped(
        val newUsername: String,
    ) : UpsertProfileAction()

    data object ProfileUpdateSucceeded : UpsertProfileAction()
    data class ProfileUpdateFailed(val reason: Throwable) : UpsertProfileAction()
    data object BackTapped : UpsertProfileAction()
}
