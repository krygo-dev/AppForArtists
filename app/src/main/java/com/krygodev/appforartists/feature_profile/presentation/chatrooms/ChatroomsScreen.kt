package com.krygodev.appforartists.feature_profile.presentation.chatrooms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krygodev.appforartists.core.presentation.components.SetupBottomNavBar

@Composable
fun ChatroomsScreen(
    navController: NavController,
    viewModel: ChatroomsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val chatroomsState = viewModel.chatrooms.value
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.padding(horizontal = 8.dp),
        bottomBar = { SetupBottomNavBar(navController = navController) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Chatrooms Screen")
        }
    }
}