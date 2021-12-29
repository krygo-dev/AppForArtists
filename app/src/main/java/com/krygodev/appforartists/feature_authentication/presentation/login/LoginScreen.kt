package com.krygodev.appforartists.feature_authentication.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Login
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krygodev.appforartists.R
import com.krygodev.appforartists.core.domain.util.UIEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val emailState = viewModel.email.value
    val passwordState = viewModel.password.value
    val scaffoldState = rememberScaffoldState()
    var passwordVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UIEvent.NavigateTo -> {
                    navController.navigate(route = event.route)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.padding(8.dp)
    ) {
        OutlinedButton(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.LightGray,
                backgroundColor = Color.Black
            )
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Wróć",
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_black_770x663),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = emailState,
                        onValueChange = { viewModel.onEvent(LoginEvent.EnteredEmail(it)) },
                        label = { Text(text = "Email") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = passwordState,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon =
                                if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                            IconButton(
                                onClick = { passwordVisibility = !passwordVisibility }
                            ) {
                                Icon(imageVector = icon, contentDescription = "Pokaż/ukryj hasło")
                            }
                        },
                        onValueChange = { viewModel.onEvent(LoginEvent.EnteredPassword(it)) },
                        label = { Text(text = "Hasło") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black,
                            trailingIconColor = Color.LightGray
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Przypomnij hasło",
                        modifier = Modifier.clickable {
                            viewModel.onEvent(LoginEvent.ResetPassword)
                        },
                        color = Color.DarkGray,
                        fontSize = 15.sp
                    )
                }
            }
            OutlinedButton(
                onClick = {
                    viewModel.onEvent(LoginEvent.SignIn)
                },
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.LightGray,
                    backgroundColor = Color.Black
                )
            ) {
                if (state.isLoading) {
                    Row(
                        modifier = Modifier.width(200.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(30.dp),
                            color = Color.LightGray
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.width(200.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Login,
                            contentDescription = "Zaloguj się",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(text = "Zaloguj się", fontSize = 20.sp)
                    }
                }
            }
        }
    }
}