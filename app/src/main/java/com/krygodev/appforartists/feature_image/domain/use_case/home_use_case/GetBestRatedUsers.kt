package com.krygodev.appforartists.feature_image.domain.use_case.home_use_case

import com.krygodev.appforartists.core.domain.model.UserModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetBestRatedUsers(
    private val _repository: HomeRepository
) {

    operator fun invoke(limit: Int): Flow<Resource<List<UserModel>>> {
        return _repository.getBestRatedUsers(limit = limit)
    }

}