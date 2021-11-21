package com.krygodev.appforartists.feature_authentication.presentation.util

data class AuthenticationState(
    val isLoading: Boolean = false,
    val result: Any? = null,
    val error: String = ""
)
