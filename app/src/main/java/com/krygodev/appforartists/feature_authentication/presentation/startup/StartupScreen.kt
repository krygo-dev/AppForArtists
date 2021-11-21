package com.krygodev.appforartists.feature_authentication.presentation.startup

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.krygodev.appforartists.core.domain.util.Screen

@Composable
fun StartupScreen(
    navController: NavController
) {
    val scaffoldState = rememberScaffoldState()
    
    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(Screen.LoginScreen.route)
                }
            ) {
                Text(text = "Zaloguj")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.RegistrationScreen.route)
                }
            ) {
                Text(text = "Zarejestruj")
            }
        }
    }
}