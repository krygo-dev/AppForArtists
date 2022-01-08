package com.krygodev.appforartists.feature_image.presentation.search

sealed class SearchEvent {
    data class EnteredTag(val tag: String): SearchEvent()
    object SubmitSearch: SearchEvent()
}
