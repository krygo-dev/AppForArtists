package com.krygodev.appforartists.feature_profile.domain.use_case.chat

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.model.ChatroomModel
import com.krygodev.appforartists.feature_profile.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetChatroomByUsersUid(
    private val _repository: ChatRepository
) {

    operator fun invoke(uid1: String, uid2: String): Flow<Resource<ChatroomModel>> {
        return _repository.getChatroomByUsersUid(uid1 = uid1, uid2 = uid2)
    }
}