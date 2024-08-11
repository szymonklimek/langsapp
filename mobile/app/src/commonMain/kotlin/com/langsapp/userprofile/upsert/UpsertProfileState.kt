package com.langsapp.userprofile.upsert

import com.langsapp.architecture.State
import com.langsapp.model.UserProfile

sealed class UpsertProfileState : State {
    data object Loading : UpsertProfileState()
    data class Input(
        val currentProfile: UserProfile?,
    ) : UpsertProfileState()
}
