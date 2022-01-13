package com.krygodev.appforartists.feature_profile.domain.use_case.chat

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class CreateChatroom(
    private val _repository: ChatRepository
) {

    operator fun invoke(chatroom: ChatroomModel): Flow<Resource<String>> {
        return _repository.createChatroom(chatroom = chatroom)
    }

}