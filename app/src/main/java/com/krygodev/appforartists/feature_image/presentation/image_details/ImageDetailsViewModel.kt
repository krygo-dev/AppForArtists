package com.krygodev.appforartists.feature_image.presentation.image_details

import android.util.Log
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
import com.krygodev.appforartists.feature_image.domain.model.CommentModel
import com.krygodev.appforartists.feature_image.domain.use_case.ImageUseCases
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
class ImageDetailsViewModel @Inject constructor(
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

    private val _imageComments = mutableStateOf(listOf<CommentModel>())
    val imageComments: State<List<CommentModel>> = _imageComments

    private val _user = mutableStateOf(UserModel())
    val user: State<UserModel> = _user

    private val _comment = mutableStateOf(CommentModel())
    val comment: State<CommentModel> = _comment

    private var _userFavorites = mutableListOf<String>()
    private var _imageLikedBy = mutableListOf<String>()

    init {
        savedStateHandle.get<String>(Constants.PARAM_IMAGE_ID)?.let { id ->
            onEvent(ImageDetailsEvent.GetImageById(id))
            onEvent(ImageDetailsEvent.GetImageComments(id))
        }
        onEvent(ImageDetailsEvent.GetUserData)
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
                                _imageLikedBy = image.value.likedBy.toMutableList()
                                Log.d("TAG", image.value.toString())
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
            is ImageDetailsEvent.GetImageComments -> {
                viewModelScope.launch {
                    _imageUseCases.getImageComments(id = event.id).onEach { result ->
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

                                _imageComments.value = result.data!!
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
            is ImageDetailsEvent.GetUserData -> {
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
                                _userFavorites = user.value.favorites.toMutableList()
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
            is ImageDetailsEvent.EnteredCommentContent -> {
                _comment.value = comment.value.copy(
                    content = event.content
                )
            }
            is ImageDetailsEvent.AddComment -> {

                _comment.value = comment.value.copy(
                    authorUid = user.value.uid,
                    authorName = user.value.username,
                    timestamp = Timestamp(Date())
                )

                viewModelScope.launch {
                    _imageUseCases.addOrEditComment(
                        comment = comment.value,
                        id = image.value.id!!
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

                                _comment.value = comment.value.copy(
                                    content = ""
                                )

                                onEvent(ImageDetailsEvent.GetImageComments(image.value.id!!))
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
            is ImageDetailsEvent.DeleteComment -> {
                viewModelScope.launch {
                    _imageUseCases.deleteComment(
                        comment = event.comment,
                        id = image.value.id!!
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
                                onEvent(ImageDetailsEvent.GetImageComments(image.value.id!!))
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
            is ImageDetailsEvent.AddImageToFavorites -> {
                _userFavorites.add(event.id)
                _user.value = user.value.copy(
                    favorites = _userFavorites
                )
                onEvent(ImageDetailsEvent.UpdateUserData(user.value))
            }
            is ImageDetailsEvent.RemoveFromFavorites -> {
                _userFavorites.remove(event.id)
                _user.value = user.value.copy(
                    favorites = _userFavorites
                )
                onEvent(ImageDetailsEvent.UpdateUserData(user.value))
            }
            is ImageDetailsEvent.LikeImage -> {
                _imageLikedBy.add(user.value.uid!!)
                _image.value = image.value.copy(
                    likes = _imageLikedBy.size,
                    likedBy = _imageLikedBy
                )
                onEvent(ImageDetailsEvent.UpdateImageLikes)
            }
            is ImageDetailsEvent.DislikeImage -> {
                _imageLikedBy.remove(user.value.uid!!)
                _image.value = image.value.copy(
                    likes = _imageLikedBy.size,
                    likedBy = _imageLikedBy
                )
                onEvent(ImageDetailsEvent.UpdateImageLikes)
            }
            is ImageDetailsEvent.UpdateImageLikes -> {
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
            is ImageDetailsEvent.UpdateUserData -> {
                viewModelScope.launch {
                    _profileUseCases.setOrUpdateUserData(user = event.user).onEach { result ->
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
        }
    }
}