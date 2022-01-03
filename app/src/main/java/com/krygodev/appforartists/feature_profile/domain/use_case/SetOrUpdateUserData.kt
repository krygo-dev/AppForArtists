package com.krygodev.appforartists.feature_profile.domain.use_case

import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SetOrUpdateUserData(
    private val _repository: ProfileRepository
) {

    operator fun invoke(user: UserModel): Flow<Resource<Void>> {

        if (user.username.isNullOrEmpty()) {
            return flow {
                emit(Resource.Error("Nazwa użytkownika nie może być pusta!"))
            }
        }

        return _repository.setOrUpdateUserData(user = user)
    }
}