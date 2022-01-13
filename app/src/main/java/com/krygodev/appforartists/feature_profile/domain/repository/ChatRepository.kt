package com.krygodev.appforartists.feature_profile.domain.repository

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    fun getUserChatrooms(uid: String): Flow<Resource<List<ChatroomModel>>>

    fun getChatroomByUsersUid(uid1: String, uid2: String): Flow<Resource<ChatroomModel>>

    fun getMessages(chatroom: ChatroomModel): Flow<Resource<List<MessageModel>>>

    fun sendMessage(chatroom: ChatroomModel, message: MessageModel): Flow<Resource<Void>>
}