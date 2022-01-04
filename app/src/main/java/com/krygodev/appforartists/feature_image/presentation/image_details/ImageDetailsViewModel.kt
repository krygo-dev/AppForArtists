package com.krygodev.appforartists.feature_image.presentation.image_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.presentation.util.LoadingState
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_image.domain.use_case.ImageUseCases
import com.krygodev.appforartists.feature_profile.domain.use_case.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailsViewModel @Inject constructor(
    private val _imageUseCases: ImageUseCases,
    _profileUseCases: ProfileUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(LoadingState())
    val state: State<LoadingState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _image = mutableStateOf(ImageModel())
    val image: State<ImageModel> = _image

    private val _currentUserUid = mutableStateOf(String())
    val currentUserUid: State<String> = _currentUserUid

    init {
        savedStateHandle.get<String>(Constants.PARAM_IMAGE_ID)?.let { id ->
            onEvent(ImageDetailsEvent.GetImageById(id))
        }
        _currentUserUid.value = _profileUseCases.getCurrentUser()!!.uid
    }

    fun onEvent(event: ImageDetailsEvent) {
        when (event) {
            is ImageDetailsEvent.GetImageById -> {
                viewModelScope.launch {
                    _imageUseCases.getImageById(imageId = event.id).onEach { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    isLoading = true,
                                    error = "",
                                    result = null
                                )
                            }
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    isLoading = false,
                                    error = "",
                                    result = result.data
                                )

                                _image.value = result.data!!
                            }
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    isLoading = false,
                                    error = result.message!!,
                                    result = null
                                )
                                _eventFlow.emit(UIEvent.ShowSnackbar(result.message))
                            }
                        }
                    }.launchIn(this)
                }
            }
            is ImageDetailsEvent.AddComment -> {

            }
            is ImageDetailsEvent.EditComment -> TODO()
            is ImageDetailsEvent.DeleteComment -> TODO()
            is ImageDetailsEvent.AddImageToFavorites -> TODO()
            is ImageDetailsEvent.RemoveFromFavorites -> TODO()
            is ImageDetailsEvent.DeleteImage -> {
                viewModelScope.launch {
                    _imageUseCases.deleteImage(image = image.value).onEach { result ->
                        when (result) {
                            is Resource.Loading -> {
                                _state.value = state.value.copy(
                                    isLoading = true,
                                    error = "",
                                    result = null
                                )
                            }
                            is Resource.Success -> {
                                _state.value = state.value.copy(
                                    isLoading = false,
                                    error = "",
                                    result = result.data
                                )
                                _eventFlow.emit(UIEvent.ShowSnackbar("Obraz usunięty!"))
                                delay(500)
                                _eventFlow.emit(UIEvent.NavigateTo(Screen.ProfileScreen.route))
                            }
                            is Resource.Error -> {
                                _state.value = state.value.copy(
                                    isLoading = false,
                                    error = result.message!!,
                                    result = null
                                )
                                _eventFlow.emit(UIEvent.ShowSnackbar(result.message))
                            }
                        }
                    }.launchIn(this)
                }
            }
        }
    }
}