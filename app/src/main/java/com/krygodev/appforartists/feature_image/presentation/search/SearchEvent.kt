package com.krygodev.appforartists.feature_image.presentation.search

sealed class SearchEvent {
    data class EnteredQuery(val query: String): SearchEvent()
    data class SubmitSearch(val selection: String): SearchEvent()
}
