package com.krygodev.appforartists.core.presentation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Screen
import com.krygodev.appforartists.feature_authentication.presentation.login.LoginScreen
import com.krygodev.appforartists.feature_authentication.presentation.register.RegistrationScreen
import com.krygodev.appforartists.feature_authentication.presentation.startup.StartupScreen
import com.krygodev.appforartists.feature_image.presentation.add_edit_image.AddEditImageScreen
import com.krygodev.appforartists.feature_image.presentation.home.HomeScreen
import com.krygodev.appforartists.feature_image.presentation.image_details.ImageDetailsScreen
import com.krygodev.appforartists.feature_image.presentation.search.SearchScreen
import com.krygodev.appforartists.feature_profile.presentation.edit_profile.EditProfileScreen
import com.krygodev.appforartists.feature_profile.presentation.messages.MessagesScreen
import com.krygodev.appforartists.feature_profile.presentation.profile.ProfileScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Constants.AUTHENTICATION_GRAPH_ROUTE,
        route = Constants.ROOT_GRAPH_ROUTE
    ) {
        authenticationNavGraph(navController = navController)
        homeNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.authenticationNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.StartupScreen.route,
        route = Constants.AUTHENTICATION_GRAPH_ROUTE
    ) {
        composable(route = Screen.StartupScreen.route) {
            StartupScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.RegistrationScreen.route) {
            RegistrationScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.HomeScreen.route,
        route = Constants.HOME_GRAPH_ROUTE
    ) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(route = Screen.ImageDetailsScreen.route) {
            ImageDetailsScreen(navController = navController)
        }
        composable(route = Screen.AddEditImageScreen.route) {
            AddEditImageScreen(navController = navController)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(route = Screen.EditProfileScreen.route) {
            EditProfileScreen(navController = navController)
        }
        composable(route = Screen.MessagesScreen.route) {
            MessagesScreen(navController = navController)
        }
    }
}