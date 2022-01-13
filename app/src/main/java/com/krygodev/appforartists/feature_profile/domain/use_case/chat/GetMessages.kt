package com.krygodev.appforartists.feature_profile.domain.use_case.chat

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel
import com.krygodev.appforartists.feature_profile.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetMessages(
    private val _repository: ChatRepository
) {

    operator fun invoke(chatroom: ChatroomModel): Flow<Resource<List<MessageModel>>> {
        return _repository.getMessages(chatroom = chatroom)
    }

}