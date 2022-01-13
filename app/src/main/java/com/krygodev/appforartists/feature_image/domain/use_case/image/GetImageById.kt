package com.krygodev.appforartists.feature_image.domain.use_case.image

import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class GetImageById(
    private val _repository: ImageRepository
) {

    operator fun invoke(imageId: String): Flow<Resource<ImageModel>> {
        return _repository.getImageById(id = imageId)
    }

}