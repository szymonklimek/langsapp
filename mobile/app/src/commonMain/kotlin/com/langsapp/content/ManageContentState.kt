package com.langsapp.content

import com.langsapp.architecture.State

sealed class ManageContentState : State {
    data object Loading : ManageContentState()
    data class Loaded(
        val hasContent: Boolean,
    ) : ManageContentState()
    data object Failure : ManageContentState()
}
