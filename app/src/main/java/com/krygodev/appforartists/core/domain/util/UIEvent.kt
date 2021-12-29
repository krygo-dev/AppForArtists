package com.krygodev.appforartists.core.domain.util

sealed class UIEvent {
    data class ShowSnackbar(val message: String): UIEvent()
    data class NavigateTo(val route: String): UIEvent()
}
