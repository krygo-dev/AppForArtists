package com.krygodev.appforartists.feature_profile.domain.use_case

import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserFavorites(
    private val _repository: ProfileRepository
) {
    operator fun invoke(user: User): Flow<Resource<List<ImageModel>>> {

        if (user.favorites.isEmpty()) {
            return flow {
                emit(Resource.Success(listOf()))
            }
        }

        return _repository.getUserImagesOrFavorites(listOfUid = user.favorites)
    }
}