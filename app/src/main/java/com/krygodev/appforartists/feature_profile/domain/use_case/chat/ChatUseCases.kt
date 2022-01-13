package com.krygodev.appforartists.feature_profile.domain.use_case.chat

data class ChatUseCases(
    val getUserChatrooms: GetUserChatrooms,
    val getChatroomByUsersUid: GetChatroomByUsersUid,
    val getMessages: GetMessages,
    val sendMessage: SendMessage
)