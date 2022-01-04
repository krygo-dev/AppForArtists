package com.krygodev.appforartists.feature_image.domain.repository

import android.net.Uri
import com.krygodev.appforartists.core.domain.model.ImageModel
import com.krygodev.appforartists.core.domain.util.Resource
import com.krygodev.appforartists.feature_image.domain.model.CommentModel
import kotlinx.coroutines.flow.Flow

interface ImageRepository {

    fun getImageById(id: String) : Flow<Resource<ImageModel>>

    fun addImage(image: ImageModel, imageUri: Uri) : Flow<Resource<Void>>

    fun editImage(image: ImageModel) : Flow<Resource<Void>>

    fun deleteImage(image: ImageModel) : Flow<Resource<Void>>

    fun getImageComments(id: String) : Flow<Resource<List<CommentModel>>>

    fun addOrEditComment(comment: CommentModel, id: String) : Flow<Resource<Void>>

    fun deleteComment(comment: CommentModel, id: String) : Flow<Resource<Void>>
}