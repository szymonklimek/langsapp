package com.langsapp.userprofile.upsert

import com.langsapp.architecture.SideEffect

sealed class UpsertProfileSideEffect : SideEffect {
    data class ConfirmProfileUpsert(val username: String) : UpsertProfileSideEffect()
}
