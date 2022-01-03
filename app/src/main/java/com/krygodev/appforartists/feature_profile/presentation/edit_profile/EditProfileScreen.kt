package com.krygodev.appforartists.feature_profile.presentation.edit_profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.krygodev.appforartists.R
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val userState = viewModel.user.value
    val scaffoldState = rememberScaffoldState()

    val selectedPhotoUriState = remember { mutableStateOf<Uri?>(null) }
    val photoUploaded = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedPhotoUriState.value = uri
    }

    selectedPhotoUriState.value?.let {
        if (!photoUploaded.value) {
            viewModel.onEvent(EditProfileEvent.UpdatePhoto(it))
            photoUploaded.value = true
        }
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
                    viewModel.onEvent(EditProfileEvent.UpdateUserData)
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
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(8.dp)
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
                        contentDescription = null,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp)
                    .padding(horizontal = 8.dp),
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Zmień zdjęcie profilowe",
                    fontSize = 15.sp,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.clickable {
                        launcher.launch("image/*")
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Nazwa użytkownika:")
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = userState.username.toString(),
                        onValueChange = { viewModel.onEvent(EditProfileEvent.UpdateUsername(it)) },
                        placeholder = { Text(text = "Nazwa użytkownika") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Black,
                            focusedLabelColor = Color.Black,
                            cursorColor = Color.Black
                        ),
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Biogram:")
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = userState.bio.toString(),
                        onValueChange = { viewModel.onEvent(EditProfileEvent.UpdateBio(it)) },
                        placeholder = { Text(text = "Biogram") },
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