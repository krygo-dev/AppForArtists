package com.krygodev.appforartists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.krygodev.appforartists.core.domain.util.Screen
import com.krygodev.appforartists.feature_authentication.presentation.login.LoginScreen
import com.krygodev.appforartists.feature_authentication.presentation.register.RegistrationScreen
import com.krygodev.appforartists.feature_authentication.presentation.startup.StartupScreen
import com.krygodev.appforartists.ui.theme.AppForArtistsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppForArtistsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.StartupScreen.route
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
            }
        }
    }
}