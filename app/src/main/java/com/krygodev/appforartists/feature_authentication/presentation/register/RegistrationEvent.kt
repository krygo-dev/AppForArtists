package com.krygodev.appforartists.feature_authentication.presentation.register

sealed class RegistrationEvent {
    data class EnteredUsername(val value: String): RegistrationEvent()
    data class EnteredEmail(val value: String): RegistrationEvent()
    data class EnteredPassword(val value: String): RegistrationEvent()
    data class EnteredRepeatPassword(val value: String): RegistrationEvent()
    object SignUp: RegistrationEvent()
}
