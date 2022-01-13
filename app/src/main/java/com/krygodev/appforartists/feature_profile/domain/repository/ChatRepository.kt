package com.krygodev.appforartists.feature_profile.domain.repository

import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun createChatroom(chatroom: ChatroomModel): Flow<Resource<String>>

    fun getUserChatrooms(uid: String): Flow<Resource<UserModel>>

    fun getMessages(chatroom: ChatroomModel): Flow<Resource<List<MessageModel>>>

    fun sendMessage(chatroom: ChatroomModel, message: MessageModel): Flow<Resource<Void>>
}