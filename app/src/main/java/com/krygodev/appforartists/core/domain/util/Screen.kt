package com.krygodev.appforartists.core.domain.util

sealed class Screen(val route: String) {
    object StartupScreen: Screen("startup_screen")
    object LoginScreen: Screen("login_screen")
    object RegistrationScreen: Screen("registration_screen")
    object HomeScreen: Screen("home_screen")
    object SearchScreen: Screen("search_screen")
    object MessagesScreen: Screen("messages_screen")
    object ProfileScreen: Screen("profile_screen")
    object EditProfileScreen: Screen("edit_profile_screen")
    object AddEditImageScreen: Screen("add_edit_image_screen")
    object ImageDetailsScreen: Screen("image_details_screen")
}
