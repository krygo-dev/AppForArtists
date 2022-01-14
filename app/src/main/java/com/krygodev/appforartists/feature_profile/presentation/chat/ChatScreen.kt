package com.krygodev.appforartists.feature_profile.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.krygodev.appforartists.R
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_profile.presentation.chat.components.ChatBubbleListItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val messagesState = viewModel.messages.value
    val chatroomState = viewModel.chatroom.value
    val messageState = viewModel.message.value
    val usersState = viewModel.users.value
    val currentUserState = viewModel.currentUser.value
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
        topBar = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
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
                        contentDescription = null
                    )
                }
                if (usersState.isNotEmpty()) {
                    val chatWithUser =
                        if (usersState[0].uid == currentUserState) usersState[1]
                        else usersState[0]

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.ProfileScreen.route + "?uid=${chatWithUser.uid}")
                        }
                    ) {
                        Text(text = "${chatWithUser.username}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = rememberImagePainter(
                                data = if (!chatWithUser.userPhotoUrl.isNullOrEmpty()) chatWithUser.userPhotoUrl
                                else R.mipmap.ic_default_profile_image,
                                builder = {
                                    transformations(CircleCropTransformation())
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .background(Color.White)
            ) {
                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = messageState.message ?: "",
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(ChatEvent.SendMessage)
                                focusManager.clearFocus()
                            }
                        ) {
                            Icon(imageVector = Icons.Filled.Send, contentDescription = null)
                        }
                    },
                    onValueChange = { viewModel.onEvent(ChatEvent.EnteredMessage(it)) },
                    placeholder = { Text(text = "Wpisz wiadomość") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black,
                        trailingIconColor = Color.Black
                    ),
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                reverseLayout = true
            ) {
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
                items(messagesState) { message ->
                    ChatBubbleListItem(
                        message = message,
                        sentByCurrentUser = message.sender == currentUserState
                    )
                }
            }
        }
    }
}