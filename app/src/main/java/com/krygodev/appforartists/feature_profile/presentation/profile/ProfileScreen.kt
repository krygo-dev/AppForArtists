package com.krygodev.appforartists.feature_profile.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.krygodev.appforartists.R
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.presentation.components.ImageListItem
import com.krygodev.appforartists.core.presentation.components.SetupBottomNavBar
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val userState = viewModel.user.value
    val userImagesState = viewModel.userImages.value
    val userFavoritesState = viewModel.userFavorites.value
    val scaffoldState = rememberScaffoldState()

    val selected = remember {
        mutableStateOf(Constants.SELECT_IMAGES)
    }

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
        bottomBar = { SetupBottomNavBar(navController = navController) },
        floatingActionButton = {
            if (selected.value == Constants.SELECT_IMAGES) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.AddEditImageScreen.route + "/-1")
                    },
                    backgroundColor = Color.Black,
                    contentColor = Color.LightGray
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
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
                    .padding(horizontal = 8.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                navController.navigate(Screen.EditProfileScreen.route)
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
                                imageVector = Icons.Filled.ManageAccounts,
                                contentDescription = null,
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        OutlinedButton(
                            onClick = {
                                viewModel.onEvent(ProfileEvent.SignOut)
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
                                imageVector = Icons.Filled.Logout,
                                contentDescription = null,
                            )
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberImagePainter(
                                data = if (!userState.userPhotoUrl.isNullOrEmpty()) userState.userPhotoUrl
                                else R.mipmap.ic_default_profile_image,
                                builder = {
                                    transformations(CircleCropTransformation())
                                }
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${userState.username}",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.W700
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                item {
                    if (!userState.bio.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "${userState.bio}",
                                maxLines = 7
                            )
                        }
                        Divider()
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                selected.value = Constants.SELECT_IMAGES
                                if (userImagesState.isNullOrEmpty()) {
                                    viewModel.onEvent(ProfileEvent.GetUserImages)
                                }
                            },
                            shape = RoundedCornerShape(50.dp),
                            border = BorderStroke(2.dp, Color.Black),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (selected.value == Constants.SELECT_IMAGES) Color.White else Color.Black,
                                backgroundColor = if (selected.value == Constants.SELECT_IMAGES) Color.Black else Color.LightGray
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.width(100.dp)
                            ) {
                                Text(text = "Dodane", fontSize = 15.sp)
                            }
                        }
                        OutlinedButton(
                            onClick = {
                                selected.value = Constants.SELECT_FAVORITES
                                if (userFavoritesState.isNullOrEmpty()) {
                                    viewModel.onEvent(ProfileEvent.GetUserFavorites)
                                }
                            },
                            shape = RoundedCornerShape(50.dp),
                            border = BorderStroke(2.dp, Color.Black),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (selected.value == Constants.SELECT_FAVORITES) Color.White else Color.Black,
                                backgroundColor = if (selected.value == Constants.SELECT_FAVORITES) Color.Black else Color.LightGray
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.width(100.dp)
                            ) {
                                Text(text = "Ulubione", fontSize = 15.sp)
                            }
                        }
                    }
                }
                if (selected.value == Constants.SELECT_IMAGES) {
                    if (userImagesState.isNotEmpty()) {
                        items(userImagesState) { userImage ->
                            ImageListItem(image = userImage, navController = navController)
                        }
                    } else {
                        item {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            ) {
                                Text(text = "Lista dodanych jest pusta!")
                            }
                        }
                    }
                } else {
                    if (userFavoritesState.isNotEmpty()) {
                        items(userFavoritesState) { userFavorite ->
                            ImageListItem(image = userFavorite, navController = navController)
                        }
                    } else {
                        item {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                            ) {
                                Text(text = "Lista ulubionych jest pusta!")
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
    }
}