package com.krygodev.appforartists.feature_authentication.presentation.startup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.krygodev.appforartists.R
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
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_black_770x663),
                contentDescription = "Logo"
            )
            Box {
                Column {
                    OutlinedButton(
                        onClick = {
                            navController.navigate(Screen.LoginScreen.route)
                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.LightGray,
                            backgroundColor = Color.Black
                        )
                    ) {
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
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = {
                            navController.navigate(Screen.RegistrationScreen.route)
                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.LightGray,
                            backgroundColor = Color.Black
                        )
                    ) {
                        Row(
                            modifier = Modifier.width(200.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Mail,
                                contentDescription = "Utwórz konto",
                                modifier = Modifier.size(30.dp)
                            )
                            Text(text = "Utwórz konto", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}