package com.krygodev.appforartists.feature_image.domain.use_case.home

import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetRandomImage(
    private val _repository: HomeRepository
) {

    operator fun invoke(): Flow<Resource<ImageModel>> {
        return _repository.getRandomImage()
    }

}