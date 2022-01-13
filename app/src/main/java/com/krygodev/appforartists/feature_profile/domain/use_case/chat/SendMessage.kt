package com.krygodev.appforartists.feature_profile.domain.use_case.chat

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.model.MessageModel
import com.krygodev.appforartists.feature_profile.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SendMessage(
    private val _repository: ChatRepository
) {

    operator fun invoke(chatroom: ChatroomModel, message: MessageModel): Flow<Resource<Void>> {
        if (message.message.isEmpty()) {
            return flow {
                emit(Resource.Error("Nie można wysłać pustej wiadomości!"))
            }
        }
        return _repository.sendMessage(chatroom = chatroom, message = message)
    }

}