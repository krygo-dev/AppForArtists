package com.krygodev.appforartists.feature_profile.presentation.chatrooms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.presentation.components.SetupBottomNavBar
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_profile.presentation.chatrooms.components.ChatroomListItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatroomsScreen(
    navController: NavController,
    viewModel: ChatroomsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val chatroomsState = viewModel.chatrooms.value
    val usersState = viewModel.users.value
    val scaffoldState = rememberScaffoldState()

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
        modifier = Modifier.padding(horizontal = 8.dp),
        bottomBar = { SetupBottomNavBar(navController = navController) }
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color.Black,
                    strokeWidth = 5.dp
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    if (chatroomsState.size == usersState.size) {
                        chatroomsState.forEach { chatroom ->
                            var userToShow = UserModel()
                            for (user in usersState) {
                                if (user.chatrooms.contains(chatroom.id)) {
                                    userToShow = user
                                    break
                                }
                            }
                            item {
                                ChatroomListItem(
                                    user = userToShow,
                                    onCLick = {
                                        navController.navigate(
                                            Screen.ChatScreen.route + "/${chatroom.id}/${chatroom.uid1}/${chatroom.uid2}"
                                        )
                                    }
                                )
                            }
                        }
                    }
//                    items(chatroomsState) { chatroom ->
//                        ChatroomListItem(chatroom = chatroom, user = usersState.)
//                    }
                    item {
                        Spacer(modifier = Modifier.height(70.dp))
                    }
                }
            }
        }
    }
}