package com.krygodev.appforartists

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
                val focusManager = LocalFocusManager.current

                val systemUIController = rememberSystemUiController()
                systemUIController.setSystemBarsColor(color = Color.White)

                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        focusManager.clearFocus()
                    }
                ) {
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