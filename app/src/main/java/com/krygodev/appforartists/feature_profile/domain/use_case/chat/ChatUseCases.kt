package com.krygodev.appforartists.feature_profile.domain.use_case.chat

data class ChatUseCases(
    val createChatroom: CreateChatroom,
    val getUserChatrooms: GetUserChatrooms,
    val getMessages: GetMessages,
    val sendMessage: SendMessage
)