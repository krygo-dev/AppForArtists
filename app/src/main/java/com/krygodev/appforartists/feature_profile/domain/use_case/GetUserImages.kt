package com.krygodev.appforartists.feature_profile.domain.use_case

import com.krygodev.appforartists.core.domain.model.Image
import com.krygodev.appforartists.core.domain.model.User
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow

class GetUserImages(
    private val _repository: ProfileRepository
) {

    operator fun invoke(user: User): Flow<Resource<List<Image>>> {
        return _repository.getUserImages(user = user)
    }
}