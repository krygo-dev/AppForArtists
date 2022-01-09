package com.krygodev.appforartists.feature_image.presentation.add_edit_image

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.presentation.util.LoadingState
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_image.domain.use_case.image_use_case.ImageUseCases
import com.krygodev.appforartists.feature_profile.domain.use_case.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddEditImageViewModel @Inject constructor(
    private val _imageUseCases: ImageUseCases,
    private val _profileUseCases: ProfileUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(LoadingState())
    val state: State<LoadingState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _image = mutableStateOf(ImageModel())
    val image: State<ImageModel> = _image

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri

    private val _user = mutableStateOf(UserModel())
    val user: State<UserModel> = _user

    private var _userImages = mutableListOf<String>()
    private var _imageTags = mutableListOf<String>()

    init {
        savedStateHandle.get<String>(Constants.PARAM_IMAGE_ID)?.let { id ->
            if (id != "-1") {
                onEvent(AddEditImageEvent.GetImageById(id))
            }
        }
        onEvent(AddEditImageEvent.GetUserData)
    }

    fun onEvent(event: AddEditImageEvent) {
        when (event) {
            is AddEditImageEvent.GetImageById -> {
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
                                _imageTags = image.value.tags.toMutableList()
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
            is AddEditImageEvent.GetUserData -> {
                viewModelScope.launch {
                    _profileUseCases.getUserData(_profileUseCases.getCurrentUser()!!.uid).onEach { result ->
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

                                _user.value = result.data!!
                                _userImages = user.value.images.toMutableList()
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
            is AddEditImageEvent.UpdateUserData -> {
                viewModelScope.launch {
                    _profileUseCases.setOrUpdateUserData(user = user.value).onEach { result ->
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
            is AddEditImageEvent.AddImage -> {

                _image.value = image.value.copy(
                    authorUsername = user.value.username,
                    authorUid = user.value.uid,
                    timestamp = Timestamp(Date()).toDate().time
                )

                viewModelScope.launch {
                    _imageUseCases.addImage(
                        image = image.value,
                        imageUri = imageUri.value
                    ).onEach { result ->
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

                                _userImages.add(result.data!!)
                                _user.value = user.value.copy(
                                    images = _userImages
                                )
                                onEvent(AddEditImageEvent.UpdateUserData)
                                _eventFlow.emit(UIEvent.ShowSnackbar("Obraz dodany pomyślnie!"))
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
            is AddEditImageEvent.EditImage -> {
                viewModelScope.launch {
                    _imageUseCases.editImage(image = image.value).onEach { result ->
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
                                _eventFlow.emit(UIEvent.ShowSnackbar("Edycja obrazu pomyślna!"))
                                delay(500)
                                _eventFlow.emit(UIEvent.NavigateTo(Screen.ImageDetailsScreen.route + "/${image.value.id}"))
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
            is AddEditImageEvent.EnteredDescription -> {
                _image.value = image.value.copy(
                    description = event.content
                )
            }
            is AddEditImageEvent.CheckTag -> {
                _imageTags.add(event.tag)
                _image.value = image.value.copy(
                    tags = _imageTags
                )
                if (_imageTags.size > 4) {
                    viewModelScope.launch {
                        _eventFlow.emit(UIEvent.ShowSnackbar("Możesz dodać maksymalnie 4 tagi!"))
                    }
                    onEvent(AddEditImageEvent.UncheckTag(event.tag))
                }
            }
            is AddEditImageEvent.UncheckTag -> {
                _imageTags.remove(event.tag)
                _image.value = image.value.copy(
                    tags = _imageTags
                )
            }
            is AddEditImageEvent.ChangeImageUri -> {
                _imageUri.value = event.uri
            }
        }
    }
}