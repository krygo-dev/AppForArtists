package com.krygodev.appforartists.feature_image.domain.use_case.image

import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetImagesByTag(
    private val _repository: ImageRepository
) {

    operator fun invoke(tag: String): Flow<Resource<List<ImageModel>>> {

        if (tag.isEmpty()) {
            return flow {
                emit(Resource.Error("Pole wyszukiwania nie może być puste!"))
            }
        }
        return _repository.getImagesByTag(tag = tag)
    }

}