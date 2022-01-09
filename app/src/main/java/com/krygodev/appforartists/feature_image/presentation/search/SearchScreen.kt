package com.krygodev.appforartists.feature_image.presentation.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.presentation.components.ImageListItem
import com.krygodev.appforartists.core.presentation.components.SetupBottomNavBar
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_profile.presentation.profile.components.ProfileListItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val imagesState = viewModel.images.value
    val usersState = viewModel.users.value
    val searchTagState = viewModel.query.value
    val scaffoldState = rememberScaffoldState()

    val selected = remember {
        mutableStateOf(Constants.SEARCH_TAG)
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
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(Color.White)
                ) {
                    val focusManager = LocalFocusManager.current

                    OutlinedTextField(
                        value = searchTagState,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.onEvent(SearchEvent.SubmitSearch(selected.value))
                                    focusManager.clearFocus()
                                }
                            ) {
                                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                            }
                        },
                        onValueChange = { viewModel.onEvent(SearchEvent.EnteredQuery(it)) },
                        placeholder = { Text(text = "Wyszukaj..") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black,
                            trailingIconColor = Color.Black
                        ),
                        maxLines = 1,
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .background(Color.White)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = {
                            selected.value = Constants.SEARCH_TAG
                        },
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (selected.value == Constants.SEARCH_TAG) Color.White else Color.Black,
                            backgroundColor = if (selected.value == Constants.SEARCH_TAG) Color.Black else Color.LightGray
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text(text = "Obraz", fontSize = 15.sp)
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            selected.value = Constants.SEARCH_USERNAME
                        },
                        shape = RoundedCornerShape(50.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (selected.value == Constants.SEARCH_USERNAME) Color.White else Color.Black,
                            backgroundColor = if (selected.value == Constants.SEARCH_USERNAME) Color.Black else Color.LightGray
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.width(100.dp)
                        ) {
                            Text(text = "Użytkownik", fontSize = 15.sp)
                        }
                    }
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
                    .padding(top = 8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
                if (imagesState.isNotEmpty() || usersState.isNotEmpty()) {
                    if (selected.value == Constants.SEARCH_TAG)
                        items(imagesState) { image ->
                            ImageListItem(image = image, navController = navController)
                        }
                    else {
                        items(usersState) { user ->
                            ProfileListItem(user = user, navController = navController)
                        }
                    }
                } else {
                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight()
                        ) {
                            Text(text = "Brak wyników!")
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