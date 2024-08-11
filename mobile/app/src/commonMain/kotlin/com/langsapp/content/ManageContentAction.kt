package com.langsapp.content

import com.langsapp.architecture.Action

sealed class ManageContentAction : Action {
    data class DataLoaded(
        val unitsCount: Int,
    ) : ManageContentAction()
    data object DataLoadingFailed : ManageContentAction()
    data object RetryTapped : ManageContentAction()
    data object DownloadUnitsTapped : ManageContentAction()
    data object DownloadUnitsSucceeded : ManageContentAction()
    data class DownloadUnitsFailed(
        val previousState: ManageContentState.Loaded,
    ) : ManageContentAction()
    data object BackTapped : ManageContentAction()
}
