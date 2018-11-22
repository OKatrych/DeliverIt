package eu.warble.deliverit.presentation.parcelinfo

sealed class LoadingStatus {
    object Loading : LoadingStatus()
    object NotLoading : LoadingStatus()
}
