package com.krygodev.appforartists.feature_profile.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.krygodev.appforartists.core.domain.util.Constants
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.core.presentation.util.LoadingState
import com.krygodev.appforartists.core.presentation.util.UIEvent
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel
import com.krygodev.appforartists.feature_profile.domain.use_case.chat.ChatUseCases
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
    private val _chatUseCases: ChatUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(LoadingState())
    val state: State<LoadingState> = _state

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _messages = mutableStateOf(listOf<MessageModel>())
    val messages: State<List<MessageModel>> = _messages

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private lateinit var _chatroom: ChatroomModel

    init {
        savedStateHandle.get<String>(Constants.PARAM_CHAT_ID)?.let { id ->
            savedStateHandle.get<String>(Constants.PARAM_USER_UID)?.let { uid ->
                savedStateHandle.get<String>(Constants.PARAM_SECOND_USER_UID)?.let { uid2 ->
                    _chatroom = ChatroomModel(id = id, uid1 = uid, uid2 = uid2)
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
    }

    fun onEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.SendMessage -> {
                viewModelScope.launch {
                    val message = MessageModel(
                        id = "-1",
                        sender = _chatroom.uid1,
                        receiver = _chatroom.uid2,
                        message = _messageText.value,
                        time = Timestamp(Date()).toDate().time
                    )
                    _chatUseCases.sendMessage(chatroom = _chatroom, message = message).onEach { result ->
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
            is ChatEvent.GetMessages -> {
                viewModelScope.launch {
                    _chatUseCases.getMessages(chatroom = _chatroom).onEach { result ->
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
                    _chatUseCases.getChatroomByUsersUid(uid1 = _chatroom.uid1!!, uid2 = _chatroom.uid2!!).onEach { result ->
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

                                _chatroom = result.data!!

                                onEvent(ChatEvent.GetMessages(_chatroom.id!!))
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
                _messageText.value = event.message
            }
        }
    }
}