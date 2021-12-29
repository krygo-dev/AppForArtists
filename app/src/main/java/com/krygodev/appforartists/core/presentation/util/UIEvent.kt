package com.krygodev.appforartists.core.presentation.util

sealed class UIEvent {
    data class ShowSnackbar(val message: String): UIEvent()
    data class NavigateTo(val route: String): UIEvent()
}
