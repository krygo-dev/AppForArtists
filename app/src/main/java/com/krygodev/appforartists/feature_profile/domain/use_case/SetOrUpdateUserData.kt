package com.krygodev.appforartists.feature_profile.domain.use_case

import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class SetOrUpdateUserData(
    private val _repository: ProfileRepository
) {

    operator fun invoke(user: User): Flow<Resource<Void>> {
        return _repository.setOrUpdateUserData(user = user)
    }
}