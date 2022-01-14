package com.krygodev.appforartists.feature_profile.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.presentation.util.LoadingState
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel
import com.krygodev.appforartists.feature_profile.domain.use_case.chat.ChatUseCases
import com.krygodev.appforartists.feature_profile.domain.use_case.profile.ProfileUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val _profileUseCases: ProfileUseCases,
    private val _chatUseCases: ChatUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(LoadingState())
    val state: State<LoadingState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _messages = mutableStateOf(listOf<MessageModel>())
    val messages: State<List<MessageModel>> = _messages

    private val _message = mutableStateOf(MessageModel())
    val message: State<MessageModel> = _message

    private val _chatroom = mutableStateOf(ChatroomModel())
    val chatroom: State<ChatroomModel> = _chatroom

    private val _users = mutableStateOf(listOf<UserModel>())
    val users: State<List<UserModel>> = _users

    var currentUser = mutableStateOf("")

    init {
        savedStateHandle.get<String>(Constants.PARAM_CHAT_ID)?.let { id ->
            savedStateHandle.get<String>(Constants.PARAM_USER_UID)?.let { uid ->
                savedStateHandle.get<String>(Constants.PARAM_SECOND_USER_UID)?.let { uid2 ->
                    _chatroom.value = ChatroomModel(id = id, uid1 = uid, uid2 = uid2)
                    onEvent(ChatEvent.GetUserData(uid = chatroom.value.uid1!!))
                    onEvent(ChatEvent.GetUserData(uid = chatroom.value.uid2!!))
                    if (id != "-1") {
                        onEvent(ChatEvent.GetMessages(chatId = id))
                    } else {
                        if (uid.isNotEmpty() && uid2.isNotEmpty()) {
                            onEvent(ChatEvent.GetChatroom(uid, uid2))
                        }
                    }
                }
            }
        }

        currentUser.value = _profileUseCases.getCurrentUser()!!.uid
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> {
                viewModelScope.launch {
                    _message.value = message.value.copy(
                        id = "-1",
                        sender = currentUser.value,
                        receiver = _chatroom.value.uid2,
                        time = Timestamp(Date()).toDate().time
                    )
                    _chatUseCases.sendMessage(chatroom = _chatroom.value, message = message.value)
                        .onEach { result ->
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

                                    _message.value = message.value.copy(
                                        message = ""
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
            is ChatEvent.GetMessages -> {
                viewModelScope.launch {
                    _chatUseCases.getMessages(chatroom = _chatroom.value).onEach { result ->
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

                                _messages.value = result.data!!
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
            is ChatEvent.GetChatroom -> {
                viewModelScope.launch {
                    _chatUseCases.getChatroomByUsersUid(
                        uid1 = _chatroom.value.uid1!!,
                        uid2 = _chatroom.value.uid2!!
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

                                _chatroom.value = result.data!!

                                _users.value[0].chatrooms = users.value[0].chatrooms.toMutableList().also {
                                    it.add(chatroom.value.id!!)
                                }
                                _users.value[1].chatrooms = users.value[1].chatrooms.toMutableList().also {
                                    it.add(chatroom.value.id!!)
                                }

                                onEvent(ChatEvent.UpdateUserData(users.value[0]))
                                onEvent(ChatEvent.UpdateUserData(users.value[1]))

                                onEvent(ChatEvent.GetMessages(_chatroom.value.id!!))
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
            is ChatEvent.EnteredMessage -> {
                _message.value = message.value.copy(
                    message = event.message
                )
            }
            is ChatEvent.GetUserData -> {
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
            is ChatEvent.UpdateUserData -> {
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