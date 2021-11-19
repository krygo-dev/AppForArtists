package com.krygodev.appforartists.core.domain.util

sealed class Screen(val route: String) {
    object StartupScreen: Screen("startup_screen")
    object LoginScreen: Screen("login_screen")
    object RegistrationScreen: Screen("registration_screen")
}
