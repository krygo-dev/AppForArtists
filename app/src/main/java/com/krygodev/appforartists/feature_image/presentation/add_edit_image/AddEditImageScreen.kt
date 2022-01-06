package com.krygodev.appforartists.feature_image.presentation.add_edit_image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_image.presentation.util.ImageTags
import kotlinx.coroutines.flow.collectLatest

@ExperimentalFoundationApi
@Composable
fun AddEditImageScreen(
    navController: NavController,
    viewModel: AddEditImageViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val imageState = viewModel.image.value
    val imageUriState = viewModel.imageUri.value
    val userState = viewModel.user.value
    val scaffoldState = rememberScaffoldState()

    val imageSelected = remember { mutableStateOf(false) }

    val tagsList = listOf(
        ImageTags.ABSTRACTION,
        ImageTags.ANIMAL,
        ImageTags.CITY,
        ImageTags.DRAWING,
        ImageTags.FANART,
        ImageTags.GRAPHICS,
        ImageTags.IMAGE,
        ImageTags.LANDSCAPE,
        ImageTags.NATURE,
        ImageTags.PAINTING,
        ImageTags.PHOTOGRAPHY,
        ImageTags.PORTRAIT
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onEvent(AddEditImageEvent.ChangeImageUri(uri))
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (imageState.id.isNullOrEmpty()) {
                        viewModel.onEvent(AddEditImageEvent.AddImage)
                    } else {
                        viewModel.onEvent(AddEditImageEvent.EditImage)
                    }
                },
                backgroundColor = Color.Black,
                contentColor = Color.LightGray
            ) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = null)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
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
                }
                Row(
                    modifier = Modifier
                        .height(250.dp)
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (imageSelected.value || !imageState.url.isNullOrEmpty()) {
                        Image(
                            painter = rememberImagePainter(
                                data =
                                if (!imageState.url.isNullOrEmpty()) imageState.url
                                else imageUriState
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text = "Wybierz obraz",
                            modifier = Modifier.clickable {
                                launcher.launch("image/*")
                                imageSelected.value = true
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (imageSelected.value) {
                        Text(
                            text = "Zmień wybrany obraz",
                            modifier = Modifier.clickable {
                                launcher.launch("image/*")
                                imageSelected.value = true
                            }
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    LazyVerticalGrid(
                        cells = GridCells.Fixed(3),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(tagsList) { tag ->
                            Row {
                                val isChecked =
                                    if (imageState.tags.contains(tag)) remember {
                                        mutableStateOf(
                                            true
                                        )
                                    }
                                    else remember { mutableStateOf(false) }

                                Checkbox(
                                    checked = isChecked.value,
                                    onCheckedChange = {
                                        isChecked.value = it
                                        if (isChecked.value) {
                                            viewModel.onEvent(AddEditImageEvent.CheckedTag(tag = tag))
                                        } else {
                                            viewModel.onEvent(AddEditImageEvent.UncheckedTag(tag = tag))
                                        }
                                    },
                                    enabled = true,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color.Black,
                                        uncheckedColor = Color.Black,
                                        checkmarkColor = Color.White
                                    )
                                )
                                Text(text = tag)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = if (imageState.description != null) imageState.description.toString() else "",
                        onValueChange = { viewModel.onEvent(AddEditImageEvent.EnteredDescription(it)) },
                        placeholder = { Text(text = "Opis") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        ),
                        maxLines = 7,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}