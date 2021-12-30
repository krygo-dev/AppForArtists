package com.krygodev.appforartists.core.presentation.util

data class LoadingState(
    val isLoading: Boolean = false,
    val result: Any? = null,
    val error: String = ""
)
