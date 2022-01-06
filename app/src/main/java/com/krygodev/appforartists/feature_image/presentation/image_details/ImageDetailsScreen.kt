package com.krygodev.appforartists.feature_image.presentation.image_details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_image.presentation.image_details.components.CommentsListItem
import com.krygodev.appforartists.feature_image.presentation.image_details.components.TagListItem
import kotlinx.coroutines.flow.collectLatest

@ExperimentalFoundationApi
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
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .background(Color.White)
            ) {
                val focusManager = LocalFocusManager.current

                OutlinedTextField(
                    value = commentState.content,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                viewModel.onEvent(ImageDetailsEvent.AddComment)
                                focusManager.clearFocus()
                            }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                navController.navigate(Screen.ProfileScreen.route)
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
                        if (imageState.authorUid == userState.uid) {
                            Row {
                                OutlinedButton(
                                    onClick = {
                                        navController.navigate(Screen.AddEditImageScreen.route + "/${imageState.id}")
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
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                OutlinedButton(
                                    onClick = {
                                        viewModel.onEvent(ImageDetailsEvent.DeleteImage)
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
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .height(250.dp)
                            .padding(top = 16.dp, bottom = 4.dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(
                                data = imageState.url,
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row {
                            Text(text = "Added by ")
                            Text(
                                text = "${imageState.authorUsername}",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Row {
                            Text(text = "Liked by ")
                            Text(
                                text = "${imageState.likes}",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Row {
                            IconButton(
                                onClick = {
                                    if (imageState.likedBy.contains(userState.uid)) {
                                        viewModel.onEvent(ImageDetailsEvent.DislikeImage)
                                    } else {
                                        viewModel.onEvent(ImageDetailsEvent.LikeImage)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector =
                                    if (imageState.likedBy.contains(userState.uid)) Icons.Filled.ThumbUp
                                    else Icons.Outlined.ThumbUp,
                                    contentDescription = null,
                                    tint = Color.Blue
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (userState.favorites.contains(imageState.id)) {
                                        viewModel.onEvent(ImageDetailsEvent.RemoveFromFavorites(id = imageState.id!!))
                                    } else {
                                        viewModel.onEvent(ImageDetailsEvent.AddImageToFavorites(id = imageState.id!!))
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector =
                                    if (userState.favorites.contains(imageState.id)) Icons.Outlined.Favorite
                                    else Icons.Outlined.FavoriteBorder,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        imageState.tags.forEach { tag ->
                            TagListItem(tag = tag)
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        Text(text = if (imageState.description.isNullOrEmpty()) "" else imageState.description)
                    }
                    Divider()
                }
                items(imageCommentsState) { comment ->
                    CommentsListItem(
                        comment = comment,
                        user = userState,
                        onDelete = { viewModel.onEvent(ImageDetailsEvent.DeleteComment(comment)) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
    }
}