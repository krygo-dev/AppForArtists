package com.krygodev.appforartists.feature_authentication.presentation.util

import com.google.firebase.auth.AuthResult

data class AuthenticationState(
    val isLoading: Boolean = false,
    val result: AuthResult? = null,
    val error: String = ""
)
