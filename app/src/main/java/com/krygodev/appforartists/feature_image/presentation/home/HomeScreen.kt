package com.krygodev.appforartists.feature_image.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.krygodev.appforartists.core.presentation.components.SetupBottomNavBar
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_image.presentation.home.components.ImageRowItem
import com.krygodev.appforartists.feature_image.presentation.home.components.ProfileRowItem
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val dailyImageState = viewModel.dailyImage.value
    val mostLikedImagesState = viewModel.mostLikedImages.value
    val bestRatedImagesState = viewModel.bestRatedImages.value
    val recentlyAddedImagesState = viewModel.recentlyAddedImages.value
    val bestRatedProfilesState = viewModel.bestRatedProfiles.value
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(text = "Codzienna dawka sztuki!", fontSize = 30.sp)
                    Box(
                        modifier = Modifier
                            .height(300.dp)
                            .padding(top = 16.dp, bottom = 4.dp)
                            .clickable {
                                navController.navigate(Screen.ImageDetailsScreen.route + "/${dailyImageState.id}")
                            }
                    ) {
                        Image(
                            painter = rememberImagePainter(
                                data = dailyImageState.url
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row {
                            Text(text = "Dodane przez ")
                            Text(
                                text = "${dailyImageState.authorUsername}",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.clickable {
                                    navController.navigate(Screen.ProfileScreen.route + "?uid=${dailyImageState.authorUid}")
                                }
                            )
                        }
                        Row {
                            Icon(
                                imageVector = Icons.Filled.ThumbUp,
                                contentDescription = null,
                                tint = Color.Blue
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${dailyImageState.likes}",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                        Row {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${(dailyImageState.starsAvg * 100).roundToInt() / 100.0f}",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text(text = "Najbardziej lubiane!", fontSize = 20.sp)
                    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                        items(mostLikedImagesState) { mostLikedImage ->
                            ImageRowItem(image = mostLikedImage, navController = navController)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text(text = "Najwyżej oceniane!", fontSize = 20.sp)
                    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                        items(bestRatedImagesState) { bestRatedImage ->
                            ImageRowItem(image = bestRatedImage, navController = navController)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text(text = "Ostatnio dodane!", fontSize = 20.sp)
                    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                        items(recentlyAddedImagesState) { recentlyAddedImage ->
                            ImageRowItem(image = recentlyAddedImage, navController = navController)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text(text = "Najlepiej oceniane profile!", fontSize = 20.sp)
                    LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                        items(bestRatedProfilesState) { bestRatedProfile ->
                            ProfileRowItem(
                                profile = bestRatedProfile,
                                navController = navController
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
    }
}