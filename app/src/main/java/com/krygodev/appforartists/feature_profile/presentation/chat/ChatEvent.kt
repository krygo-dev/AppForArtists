package com.krygodev.appforartists.feature_profile.presentation.chat

import com.krygodev.appforartists.core.domain.model.UserModel

sealed class ChatEvent {
    data class GetMessages(val chatId: String) : ChatEvent()
    data class GetChatroom(val uid1: String, val uid2: String) : ChatEvent()
    data class EnteredMessage(val message: String) : ChatEvent()
    data class GetUserData(val uid: String) : ChatEvent()
    data class UpdateUserData(val user: UserModel) : ChatEvent()
    object SendMessage : ChatEvent()
}