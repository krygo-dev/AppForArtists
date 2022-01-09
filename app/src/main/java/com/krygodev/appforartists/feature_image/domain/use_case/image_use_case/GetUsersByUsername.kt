package com.krygodev.appforartists.feature_image.domain.use_case.image_use_case

import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUsersByUsername(
    private val _repository: ImageRepository
) {

    operator fun invoke(username: String): Flow<Resource<List<UserModel>>> {

        if (username.isEmpty()) {
            return flow {
                emit(Resource.Error("Pole wyszukiwania nie może być puste!"))
            }
        }
        return _repository.getUsersByUsername(username = username)
    }

}