package com.krygodev.appforartists.feature_image.presentation.add_edit_image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.krygodev.appforartists.core.presentation.util.UIEvent
import kotlinx.coroutines.flow.collectLatest

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
        modifier = Modifier.padding(horizontal = 8.dp)
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
        }
    }
}