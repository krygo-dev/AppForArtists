package com.krygodev.appforartists.feature_image.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.presentation.util.LoadingState
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_image.domain.use_case.home_use_case.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _homeUseCases: HomeUseCases
) : ViewModel() {

    private val _state = mutableStateOf(LoadingState())
    val state: State<LoadingState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _dailyImage = mutableStateOf(ImageModel())
    val dailyImage: State<ImageModel> = _dailyImage

    private val _mostLikedImages = mutableStateOf(listOf<ImageModel>())
    val mostLikedImages: State<List<ImageModel>> = _mostLikedImages

    private val _bestRatedImages = mutableStateOf(listOf<ImageModel>())
    val bestRatedImages: State<List<ImageModel>> = _bestRatedImages

    private val _recentlyAddedImages = mutableStateOf(listOf<ImageModel>())
    val recentlyAddedImages: State<List<ImageModel>> = _recentlyAddedImages

    private val _bestRatedProfiles = mutableStateOf(listOf<UserModel>())
    val bestRatedProfiles: State<List<UserModel>> = _bestRatedProfiles

    init {
        getDailyImage()
        getMostLikedImages()
        getBestRatedImages()
        getRecentlyAddedImages()
        getBestRatedProfiles()
    }

    private fun getDailyImage() {
        viewModelScope.launch {
            _homeUseCases.getRandomImage().onEach { result ->
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

                        _dailyImage.value = result.data!!
                        Log.e("DAILY", dailyImage.value.toString())
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

    private fun getMostLikedImages() {
        viewModelScope.launch {
            _homeUseCases.getMostLikedImages(limit = 3).onEach { result ->
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

                        _mostLikedImages.value = result.data!!
                        Log.e("LIKED", mostLikedImages.value.toString())
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

    private fun getBestRatedImages() {
        viewModelScope.launch {
            _homeUseCases.getBestRatedImages(limit = 3).onEach { result ->
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

                        _bestRatedImages.value = result.data!!
                        Log.e("RATED", bestRatedImages.value.toString())
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

    private fun getRecentlyAddedImages() {
        viewModelScope.launch {
            _homeUseCases.getRecentlyAddedImages(limit = 3).onEach { result ->
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

                        _recentlyAddedImages.value = result.data!!
                        Log.e("ADDED", recentlyAddedImages.value.toString())
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

    private fun getBestRatedProfiles() {
        viewModelScope.launch {
            _homeUseCases.getBestRatedUsers(limit = 3).onEach { result ->
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

                        _bestRatedProfiles.value = result.data!!
                        Log.e("RATED", bestRatedProfiles.value.toString())
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