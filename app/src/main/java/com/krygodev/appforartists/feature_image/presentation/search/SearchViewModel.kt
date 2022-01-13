package com.krygodev.appforartists.feature_image.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.presentation.util.LoadingState
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_image.domain.use_case.image.ImageUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val _imageUseCases: ImageUseCases
) : ViewModel() {

    private val _state = mutableStateOf(LoadingState())
    val state: State<LoadingState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _images = mutableStateOf(listOf<ImageModel>())
    val images: State<List<ImageModel>> = _images

    private val _users = mutableStateOf(listOf<UserModel>())
    val users: State<List<UserModel>> = _users

    private val _query = mutableStateOf("")
    val query: State<String> = _query

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.EnteredQuery -> {
                _query.value = event.query.replace(" ", "")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
            is SearchEvent.SubmitSearch -> {
                if (event.selection == Constants.SEARCH_TAG) {
                    searchForImagesByTag(tag = query.value)
                } else {
                    searchForUsersByUsername(username = query.value)
                }
            }
        }
    }

    private fun searchForUsersByUsername(username: String) {
        viewModelScope.launch {
            _imageUseCases.getUsersByUsername(username = username).onEach { result ->
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

                        _users.value = result.data!!
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

    private fun searchForImagesByTag(tag: String) {
        viewModelScope.launch {
            _imageUseCases.getImagesByTag(tag = tag).onEach { result ->
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

                        _images.value = result.data!!
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
