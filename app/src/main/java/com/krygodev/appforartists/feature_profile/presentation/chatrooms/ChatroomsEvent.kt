package com.krygodev.appforartists.feature_profile.presentation.chatrooms

sealed class ChatroomsEvent {
    object GetUserChatrooms: ChatroomsEvent()
}
