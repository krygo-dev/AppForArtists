package com.krygodev.appforartists.feature_profile.domain.use_case.chat

import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetUserChatrooms(
    private val _repository: ChatRepository
) {

    operator fun invoke(uid: String): Flow<Resource<UserModel>> {
        return _repository.getUserChatrooms(uid = uid)
    }

}