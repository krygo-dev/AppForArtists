package com.krygodev.appforartists.feature_image.domain.use_case

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.model.CommentModel
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow

class AddOrEditComment(
    private val _repository: ImageRepository
) {

    operator fun invoke(comment: CommentModel, id: String): Flow<Resource<Void>> {
        return _repository.addOrEditComment(comment = comment, id = id)
    }

}