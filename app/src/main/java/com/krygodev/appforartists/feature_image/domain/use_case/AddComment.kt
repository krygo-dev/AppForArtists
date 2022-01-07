package com.krygodev.appforartists.feature_image.domain.use_case

import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.model.CommentModel
import com.krygodev.appforartists.feature_image.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddComment(
    private val _repository: ImageRepository
) {

    operator fun invoke(comment: CommentModel, id: String): Flow<Resource<Void>> {
        if (comment.content.isEmpty()) {
            return flow {
                emit(Resource.Error("Komentarz musi zawierać treść!"))
            }
        }
        return _repository.addComment(comment = comment, id = id)
    }

}