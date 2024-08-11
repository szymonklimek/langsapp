package com.langsapp.content

import com.langsapp.architecture.SideEffect

sealed interface ManageContentSideEffect {
    data object LoadContentData : SideEffect
    data class DownloadUnits(
        val previousState: ManageContentState.Loaded,
    ) : SideEffect
}
