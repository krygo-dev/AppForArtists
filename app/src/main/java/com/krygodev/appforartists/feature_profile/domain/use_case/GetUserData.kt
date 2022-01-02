package com.krygodev.appforartists.feature_profile.domain.use_case

import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetUserData(
    private val _repository: ProfileRepository
) {

    operator fun invoke(uid: String): Flow<Resource<UserModel>> {
        return _repository.getUserData(uid = uid)
    }

}