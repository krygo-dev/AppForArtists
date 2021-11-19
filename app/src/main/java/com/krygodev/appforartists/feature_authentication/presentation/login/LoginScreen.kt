package com.krygodev.appforartists.feature_authentication.presentation.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krygodev.appforartists.core.domain.util.UIEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val emailState = viewModel.email.value
    val passwordState = viewModel.password.value
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Wprowadź adres email oraz hasło.")
            OutlinedTextField(
                value = emailState,
                onValueChange = { viewModel.onEvent(LoginEvent.EnteredEmail(it)) },
                placeholder = { Text(text = "Email") }
            )
            OutlinedTextField(
                value = passwordState,
                onValueChange = { viewModel.onEvent(LoginEvent.EnteredPassword(it)) },
                placeholder = { Text(text = "Hasło") }
            )
            Button(
                onClick = {
                    viewModel.onEvent(LoginEvent.SignIn)
                }
            ) {
                Text(text = "Sign In")
            }
        }
    }
}