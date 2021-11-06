package com.krygodev.appforartists.feature_authentication.presentation.util

sealed class AuthenticationState<T> {
    class Loading<T> : AuthenticationState<T>()
    data class Success<T>(val message: String) : AuthenticationState<T>()
    data class Error<T>(val message: String) : AuthenticationState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(message: String) = Success<T>(message)
        fun <T> error(message: String) = Error<T>(message)
    }
}
