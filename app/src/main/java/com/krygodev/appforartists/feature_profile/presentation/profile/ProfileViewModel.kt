package com.krygodev.appforartists.feature_profile.presentation.profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.presentation.util.LoadingState
import com.krygodev.appforartists.core.presentation.util.Screen
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_authentication.domain.use_case.AuthenticationUseCases
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
class ProfileViewModel @Inject constructor(
    private val _profileUseCases: ProfileUseCases,
    private val _authenticationUseCases: AuthenticationUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(LoadingState())
    val state: State<LoadingState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _user = mutableStateOf(UserModel())
    val user: State<UserModel> = _user

    private val _userImages = mutableStateOf(listOf<ImageModel>())
    val userImages: State<List<ImageModel>> = _userImages

    private val _userFavorites = mutableStateOf(listOf<ImageModel>())
    val userFavorites: State<List<ImageModel>> = _userFavorites

    val currentUser = mutableStateOf("")

    private var _userStarredBy = mutableListOf<String>()

    init {
        currentUser.value = _profileUseCases.getCurrentUser()!!.uid
        savedStateHandle.get<String>(Constants.PARAM_USER_UID)?.let { uid ->
            if (uid != "-1") {
                onEvent(ProfileEvent.GetUserData(uid))
            } else {
                onEvent(ProfileEvent.GetUserData(currentUser.value))
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.GetUserData -> {
                viewModelScope.launch {
                    _profileUseCases.getUserData(event.uid).onEach { result ->
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
                                _userStarredBy = user.value.starredBy.toMutableList()

                                onEvent(ProfileEvent.GetUserImages)
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
            is ProfileEvent.GetUserImages -> {
                viewModelScope.launch {
                    _profileUseCases.getUserImages(user.value).onEach { result ->
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
                                _userImages.value = result.data!!

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
            is ProfileEvent.GetUserFavorites -> {
                viewModelScope.launch {
                    _profileUseCases.getUserFavorites(user.value).onEach { result ->
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
                                _userFavorites.value = result.data!!

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
            is ProfileEvent.SignOut -> {
                viewModelScope.launch {
                    _authenticationUseCases.signOut().onEach { result ->
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

                                _eventFlow.emit(UIEvent.ShowSnackbar("Wylogowano!"))
                                delay(500)
                                _eventFlow.emit(UIEvent.NavigateTo(Screen.StartupScreen.route))
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
            is ProfileEvent.AddStars -> {
                _userStarredBy.add(currentUser.value)
                val sum = user.value.starsSum + event.count

                _user.value = user.value.copy(
                    starredBy = _userStarredBy,
                    starsSum = sum,
                    starsAvg = sum / _userStarredBy.size.toFloat()
                )

                onEvent(ProfileEvent.UpdateUserData)
            }
            is ProfileEvent.UpdateUserData -> {
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
        }
    }
}