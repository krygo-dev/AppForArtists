package com.krygodev.appforartists.feature_image.presentation.image_details

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krygodev.appforartists.core.presentation.util.UIEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ImageDetailsScreen(
    navController: NavController,
    viewModel: ImageDetailsViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val imageState = viewModel.image.value
    val userState = viewModel.user.value
    val imageCommentsState = viewModel.imageComments.value
    val commentState = viewModel.comment.value
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
        bottomBar = {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                OutlinedTextField(
                    value = commentState.content,
                    trailingIcon = {
                        IconButton(
                            onClick = { viewModel.onEvent(ImageDetailsEvent.AddComment) }
                        ) {
                            Icon(imageVector = Icons.Filled.Send, contentDescription = null)
                        }
                    },
                    onValueChange = { viewModel.onEvent(ImageDetailsEvent.EnteredCommentContent(it)) },
                    label = { Text(text = "Wpisz komentarz") },
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "$imageState")
            }
        }
    }
}