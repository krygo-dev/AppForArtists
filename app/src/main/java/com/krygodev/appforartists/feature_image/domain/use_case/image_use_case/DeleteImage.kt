package com.krygodev.appforartists.feature_image.domain.use_case.image_use_case

import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class DeleteImage(
    private val _repository: ImageRepository
) {

    operator fun invoke(image: ImageModel): Flow<Resource<Void>> {
        return _repository.deleteImage(image = image)
    }

}