package com.krygodev.appforartists.feature_authentication.presentation.login

sealed class LoginEvent {
    data class EnteredEmail(val value: String): LoginEvent()
    data class EnteredPassword(val value: String): LoginEvent()
    object SignIn: LoginEvent()
    object ResetPassword: LoginEvent()
}
