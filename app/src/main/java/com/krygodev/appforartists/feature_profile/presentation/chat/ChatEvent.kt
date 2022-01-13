package com.krygodev.appforartists.feature_profile.presentation.chat

sealed class ChatEvent {
    data class GetMessages(val chatId: String) : ChatEvent()
    data class GetChatroom(val uid1: String, val uid2: String) : ChatEvent()
    data class EnteredMessage(val message: String) : ChatEvent()
    object SendMessage : ChatEvent()
}