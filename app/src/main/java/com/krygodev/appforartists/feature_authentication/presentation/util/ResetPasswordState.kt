package com.krygodev.appforartists.feature_authentication.presentation.util

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val result: Void? = null,
    val error: String = ""
)