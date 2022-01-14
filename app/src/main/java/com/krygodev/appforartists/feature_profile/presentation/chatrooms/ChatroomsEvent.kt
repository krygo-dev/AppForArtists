package com.krygodev.appforartists.feature_profile.presentation.chatrooms

sealed class ChatroomsEvent {
    data class GetUserData(val uid: String): ChatroomsEvent()
    object GetUserChatrooms: ChatroomsEvent()
}
