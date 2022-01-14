package com.krygodev.appforartists.feature_profile.presentation.chatrooms

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.presentation.util.LoadingState
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.use_case.chat.ChatUseCases
import com.krygodev.appforartists.feature_profile.domain.use_case.profile.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatroomsViewModel @Inject constructor(
    private val _chatUseCases: ChatUseCases,
    private val _profileUseCases: ProfileUseCases
) : ViewModel() {

    private val _state = mutableStateOf(LoadingState())
    val state: State<LoadingState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _chatrooms = mutableStateOf(listOf<ChatroomModel>())
    val chatrooms: State<List<ChatroomModel>> = _chatrooms

    private val _users = mutableStateOf(listOf<UserModel>())
    val users: State<List<UserModel>> = _users

    init {
        onEvent(ChatroomsEvent.GetUserChatrooms)
    }

    fun onEvent(event: ChatroomsEvent) {
        when (event) {
            is ChatroomsEvent.GetUserChatrooms -> {
                viewModelScope.launch {
                    val currentUser = _profileUseCases.getCurrentUser()
                    _chatUseCases.getUserChatrooms(currentUser!!.uid).onEach { result ->
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

                                _chatrooms.value = result.data!!

                                chatrooms.value.forEach { chatroom ->
                                    val uid = if (chatroom.uid1 == currentUser.uid) chatroom.uid2 else chatroom.uid1
                                    onEvent(ChatroomsEvent.GetUserData(uid!!))
                                }
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
            is ChatroomsEvent.GetUserData -> {
                viewModelScope.launch {
                    _profileUseCases.getUserData(uid = event.uid).onEach { result ->
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
                                _users.value = users.value.toMutableList().also {
                                    it.add(result.data!!)
                                }
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