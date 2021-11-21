package com.krygodev.appforartists.feature_authentication.presentation.register


import androidx.compose.foundation.layout.*
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
fun RegistrationScreen(
    navController: NavController,
    viewModel: RegistrationViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val emailState = viewModel.email.value
    val passwordState = viewModel.password.value
    val repeatPasswordState = viewModel.repeatPassword.value
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
                onValueChange = { viewModel.onEvent(RegistrationEvent.EnteredEmail(it)) },
                label = { Text(text = "Email") },
                singleLine = true
            )
            OutlinedTextField(
                value = passwordState,
                onValueChange = { viewModel.onEvent(RegistrationEvent.EnteredPassword(it)) },
                label = { Text(text = "Hasło") },
                singleLine = true
            )
            OutlinedTextField(
                value = repeatPasswordState,
                onValueChange = { viewModel.onEvent(RegistrationEvent.EnteredRepeatPassword(it)) },
                label = { Text(text = "Powtórz hasło") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.onEvent(RegistrationEvent.SignUp)
                }
            ) {
                Text(text = "Zarejestruj")
            }
        }
    }
}