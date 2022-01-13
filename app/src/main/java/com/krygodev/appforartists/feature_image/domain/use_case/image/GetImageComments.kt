package com.krygodev.appforartists.feature_image.domain.use_case.image

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.model.CommentModel
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class GetImageComments(
    private val _repository: ImageRepository
) {

    operator fun invoke(id: String): Flow<Resource<List<CommentModel>>> {
        return _repository.getImageComments(id = id)
    }

}